/**
 * 
 */
package optimumPath.opengl;


import java.awt.DisplayMode;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;

import optimumPath.object.*;
import optimumPath.common.*;

/**
 * @author 
 *
 */

public class Render implements GLEventListener{

	private Map renderMap;
	private MapEdition editionMap;
	private Camera camera;
	private GLU glu;
    final private GLCanvas glcanvas;
    
    private int offsetLayer = 0;
    private boolean isMapCreation = false;
    
	public static DisplayMode dm, dm_old;
	
	public Render() {
		//getting the capabilities object of GL2 profile
	    final GLProfile profile = GLProfile.get(GLProfile.GL2);
	    GLCapabilities capabilities = new GLCapabilities(profile);
	    
	    this.camera = new Camera();		// Kamera dla glcanvas
	    this.renderMap = new Map();  	// Mapa zawierj¹ca informacje o przeszkodach œcie¿ce itp.
	    this.editionMap = new MapEdition();
	    this.glcanvas = new GLCanvas(capabilities); // canvas
	    this.glu = new GLU();  		// glu
		    
	    // inicjalizacja prametrow
	    camera.setPointPos(new Point3d(0.0, 0.0, 5.0));
		
		glcanvas.addGLEventListener(this);
	    glcanvas.addMouseListener(camera);
	    glcanvas.addMouseMotionListener(camera);
	    glcanvas.addMouseWheelListener(camera);
	    
	    glcanvas.addMouseListener(editionMap);
	    glcanvas.addMouseMotionListener(editionMap);
	    glcanvas.setSize(100, 100);
	    
		final FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);
		
	    animator.start();
	}
	
	
	
	@Override
	public void display(GLAutoDrawable drawable) {
		//MouseEvent mouse = camera.getMouse();
		
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		
		if (editionMap.isClicked() && isMapCreation) {
			editionMap.getCoordinatesFromCanvas(gl, glu);
			Point3d raster = editionMap.getPointRaster(renderMap.getSizeRaster(), offsetLayer);
			if(renderMap.isPointInMap(raster))
				renderMap.setRaster((int)raster.getX(), (int)raster.getY(), (int)raster.getZ(), Map.Raster.OBSTACLE);
		}
		
		drawBase(gl);
		drawGrid(gl);
		drawObsticles(gl);
		drawStratEnd(gl);
		
		gl.glLoadIdentity();
		gl.glTranslatef( 0.0f, 0.0f, 0.0f ); 
		
		glu.gluLookAt(camera.getPointPos().getX(), camera.getPointPos().getY(), camera.getPointPos().getZ(),
					  camera.getPointCenter().getX(), camera.getPointCenter().getY(), camera.getPointCenter().getZ(),
					  camera.getVectorUp().getX(), camera.getVectorUp().getY(), camera.getVectorUp().getZ());
		
		gl.glFlush();
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {
	   //method body
	}
		
	@Override
	public void init(GLAutoDrawable drawable) {
			
		final GL2 gl = drawable.getGL().getGL2();
		gl.glShadeModel( GL2.GL_SMOOTH );
		gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
		gl.glClearDepth( 1.0f );
		gl.glEnable( GL2.GL_DEPTH_TEST );
		//przezroczystosc
		gl.glEnable (GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glDepthFunc( GL2.GL_LESS );
		gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
		//gl.glEnable (GL2.GL_COLOR_MATERIAL);
		//gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
		//gl.glMaterialf(GL2.GL_SHININESS, 100.0);
		
		//definicja œwiat³a
		float light_ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float light_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float light_position[] = { 0.0f, 0.0f, 2.0f, 1.0f };
		
		float lmodel_ambient[] = { 0.4f, 0.4f, 0.4f, 1.0f };
        float local_view[] = { 0.0f };
		
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_ambient,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position,0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);
		
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glLoadIdentity();
	}
		
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if( height == 0 )
			height = 1;
					
		final float h = ( float ) width / ( float ) height;
		gl.glViewport( 0, 0, width, height );
		gl.glMatrixMode( GL2.GL_PROJECTION );
		gl.glLoadIdentity();
				
		glu.gluPerspective( 45.0f, h, 1.0, 200.0 );
		gl.glMatrixMode( GL2.GL_MODELVIEW );
		gl.glLoadIdentity();
	}
	
	
	///////////////////////////////////////////////////
	// rysowanie podstawy
	public void drawBase(GL2 gl) {
		
		float mat_diffuse[] = { 0.6f, 0.5f, 0.8f, 1.0f };
		float halfSizeRaster = (float)renderMap.getSizeRaster() / 2;
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
		gl.glBegin( GL2.GL_QUADS );
		
	    gl.glVertex3f( -halfSizeRaster, -halfSizeRaster, -halfSizeRaster ); 
	    gl.glVertex3f( -halfSizeRaster, -halfSizeRaster, (float)renderMap.getSizeRaster()*(float)renderMap.getSizeY()-halfSizeRaster );
	    gl.glVertex3f( (float)renderMap.getSizeRaster()*(float)renderMap.getSizeX()-halfSizeRaster, -halfSizeRaster, (float)renderMap.getSizeRaster()*(float)renderMap.getSizeY()-halfSizeRaster);
	    gl.glVertex3f( (float)renderMap.getSizeRaster()*(float)renderMap.getSizeX()-halfSizeRaster, -halfSizeRaster, -halfSizeRaster );
	    
	    gl.glEnd();
		gl.glPopMatrix();
	}
	
	////////////////////////
	// rysowanie siatki
	public void drawGrid(GL2 gl) {
		
		float mat_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float halfSizeRaster = (float)renderMap.getSizeRaster() / 2;
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
				
		for (int i = 0; i < renderMap.getSizeX() + 1; i++) {
			gl.glBegin( GL2.GL_LINES );
			
			gl.glVertex3f( -halfSizeRaster + i*(float)renderMap.getSizeRaster(), -halfSizeRaster+0.002f, -halfSizeRaster );
			gl.glVertex3f( -halfSizeRaster + i*(float)renderMap.getSizeRaster(), -halfSizeRaster+0.002f, (float)renderMap.getSizeRaster()*(float)renderMap.getSizeY()-halfSizeRaster );
			
			gl.glEnd();
		}
				
		for (int i = 0; i < renderMap.getSizeY() + 1; i++) {
			gl.glBegin( GL2.GL_LINES );
			
			gl.glVertex3f( -halfSizeRaster, -halfSizeRaster+0.002f, -halfSizeRaster + i*(float)renderMap.getSizeRaster());
			gl.glVertex3f( (float)renderMap.getSizeRaster()*(float)renderMap.getSizeX()-halfSizeRaster, -halfSizeRaster+0.002f, -halfSizeRaster + i*(float)renderMap.getSizeRaster());
			
			gl.glEnd();
		}
		gl.glPopMatrix();
		
	}
	
	////////////////////////
	// rysowanie przeszkód
	public void drawObsticles(GL2 gl) {
		GLUT glut = new GLUT();
		
		float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float mat_ambient_color[] = { 0.8f, 0.8f, 0.2f, 1.0f };
		float mat_diffuse[] = { 0.1f, 0.5f, 0.8f, 0.8f }; //kolor, ostatni parametr okresla przezroczystosc
		float mat_diffuse_modification[] = { 0.5f, 0.1f, 0.8f, 0.5f }; //kolor, ostatni parametr okresla przezroczystosc
		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float low_shininess[] = { 5.0f };
		
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_ambient_color, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, low_shininess, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, no_mat, 0);
	    
		for (int index = 0; index < renderMap.getObstacleShift().size(); index++) {
			
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
			//kostka o okreœlonym materiale
			Point3d translate = renderMap.getObstacleShift().get(index);
			Point3d raster = renderMap.pointFromShift(renderMap.getObstacleShift(), index);
			if(this.isMapCreation && (int)raster.getZ() > offsetLayer)
				continue;
			if(this.isMapCreation && (int)raster.getZ() == offsetLayer)
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse_modification, 0);
			
			gl.glPushMatrix();
			gl.glTranslatef((float)translate.getX(), (float)translate.getY(), (float)translate.getZ());
			
			glut.glutSolidCube((float)renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
	}
	
	public void drawStratEnd(GL2 gl) {
		GLUT glut = new GLUT();
		
		float mat_diffuse1[] = { 1.0f, 0.8f, 0.1f, 1.0f };
		float mat_diffuse2[] = { 1.0f, 0.2f, 0.1f, 1.0f };
		
		if (renderMap.isStart()) {
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse1, 0);
			Point3d translate = renderMap.getStartShift();
			gl.glPushMatrix();
			gl.glTranslatef((float)translate.getX(), (float)translate.getY(), (float)translate.getZ());
			glut.glutSolidCube((float)renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
		
		if (renderMap.isEnd()) {
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse2, 0);
			Point3d translate = renderMap.getEndShift();
			gl.glPushMatrix();
			gl.glTranslatef((float)translate.getX(), (float)translate.getY(), (float)translate.getZ());
			glut.glutSolidCube((float)renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
			
	}
	
	////////////////////////////////////////
	// ---------- get i set --------------
	
	public Map getRenderMap() {
		return renderMap;
	}

	public void setRenderMap(Map renderMap) {
		this.renderMap = renderMap;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public GLU getGlu() {
		return glu;
	}

	public void setGlu(GLU glu) {
		this.glu = glu;
	}

	public GLCanvas getGlcanvas() {
		return glcanvas;
	}

	public int getOffsetLayer() {
		return offsetLayer;
	}

	public void setOffsetLayer(int offsetLayer) {
		this.offsetLayer = offsetLayer;
	}

	public boolean isMapCreation() {
		return isMapCreation;
	}

	public void setMapCreation(boolean isMapCreation) {
		this.isMapCreation = isMapCreation;
		this.camera.setMapCreation(isMapCreation);
	}
	
	
	
	//////////////////
	
}
