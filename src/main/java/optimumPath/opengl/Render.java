/**
 * 
 */
package optimumPath.opengl;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.GLBuffers;

import optimumPath.object.*;
import optimumPath.window.WindowMain;
import optimumPath.common.*;

/**
 * @author
 *
 */

public class Render implements GLEventListener {

	private Map renderMap;
	private MapEdition editionMap;
	private Camera camera;
	private MaterialsList materials;
	private GLU glu;
	final private GLCanvas glcanvas;
	private ByteBuffer buffer;

	private int offsetLayer = 0;
	private boolean isMapCreation = false;
	private boolean isAnimation = false;
	private boolean isAStar = false;

	public static DisplayMode dm, dm_old;
	private WindowMain window;

	public Render() {
		// getting the capabilities object of GL2 profile
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);

		this.camera = new Camera(); // Kamera dla glcanvas
		this.renderMap = new Map(); // Mapa zawierj¹ca informacje o przeszkodach œcie¿ce itp.
		this.editionMap = new MapEdition(); //Obiekt zawieraj¹cy metody do modyfikacji mapy np. odczyt wspolrzednych mapy
		this.materials = new MaterialsList();
		this.glcanvas = new GLCanvas(capabilities); // canvas
		this.glu = new GLU(); // glu

		// inicjalizacja prametrow
		camera.setPointPos(new Point3d(0.0, 0.0, 5.0));

		glcanvas.addGLEventListener(this);
		glcanvas.addMouseListener(camera);
		glcanvas.addMouseMotionListener(camera);
		glcanvas.addMouseWheelListener(camera);

		glcanvas.addMouseListener(editionMap);
		glcanvas.addMouseMotionListener(editionMap);
		glcanvas.setSize(100, 100);
		
		final FPSAnimator animator = new FPSAnimator(glcanvas, 60, true);
		renderMap.setAnimator(animator);
		
		animator.start();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// MouseEvent mouse = camera.getMouse();

		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		if (editionMap.isClicked() && isMapCreation && editionMap.getMouseButton() == 1) {
			editionMap.getCoordinatesFromCanvas(gl, glu);
			editionMap.modifcationMap(renderMap, offsetLayer);
		}

		drawBase(gl);
		if (isMapCreation)
			drawGrid(gl, offsetLayer + 1);
		else
			drawGrid(gl, 0);

		drawObsticles(gl);
		drawStratEnd(gl);
		drawPath(gl);
		
		if (renderMap.isAnimation()) {
			if (isAStar)
				drawActual(gl);
			
			drawClosed(gl);
			drawOpen(gl);
		}
		
		if (renderMap.getAlgProcessor() != null)
			if(renderMap.getAlgProcessor().isFinish()) {
				renderMap.resultAlgorithm();
				window.getResults();
			}
		
		materials.setGLBackground(gl);
		//gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		glu.gluLookAt(camera.getPointPos().getX(), camera.getPointPos().getY(), camera.getPointPos().getZ(),
				camera.getPointCenter().getX(), camera.getPointCenter().getY(), camera.getPointCenter().getZ(),
				camera.getVectorUp().getX(), camera.getVectorUp().getY(), camera.getVectorUp().getZ());

		gl.glFlush();

		int viewport[] = new int[4];
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
		buffer = GLBuffers.newDirectByteBuffer(viewport[2] * viewport[3] * 4);

		gl.glReadBuffer(GL2.GL_BACK);
		gl.glReadPixels(0, 0, viewport[2], viewport[3], GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// method body
	}

	@Override
	public void init(GLAutoDrawable drawable) {

		final GL2 gl = drawable.getGL().getGL2();
		gl.glShadeModel(GL2.GL_SMOOTH);
		materials.setGLBackground(gl);
		gl.glClearDepth(1.0f);
		// przezroczystosc
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LESS);
		//gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		//gl.glEnable (GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);

		// definicja œwiat³a
		float light_ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float light_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float light_position[] = { 6.0f, 9.0f, 10.0f, 9.0f };

		float lmodel_ambient[] = { 0.4f, 0.4f, 0.4f, 1.0f };
		float local_view[] = { 0.0f };

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glLoadIdentity();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if (height == 0)
			height = 1;

		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(45.0f, h, 1.0, 200.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	///////////////////////////////////////////////////
	// rysowanie podstawy
	public void drawBase(GL2 gl) {
		float halfSizeRaster = (float) renderMap.getSizeRaster() / 2;
		
		materials.getMatBase().setGLMaterial(gl);
		
		gl.glPushMatrix();
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glVertex3f(-halfSizeRaster, -halfSizeRaster, -halfSizeRaster);
		gl.glVertex3f(-halfSizeRaster, -halfSizeRaster,
				(float) renderMap.getSizeRaster() * (float) renderMap.getSizeY() - halfSizeRaster);
		gl.glVertex3f((float) renderMap.getSizeRaster() * (float) renderMap.getSizeX() - halfSizeRaster,
				-halfSizeRaster,
				(float) renderMap.getSizeRaster() * (float) renderMap.getSizeY() - halfSizeRaster);
		gl.glVertex3f((float) renderMap.getSizeRaster() * (float) renderMap.getSizeX() - halfSizeRaster,
				-halfSizeRaster, -halfSizeRaster);

		gl.glEnd();
		gl.glPopMatrix();
	}

	////////////////////////
	// rysowanie siatki
	public void drawGrid(GL2 gl, int layer) {

		float halfSizeRaster = (float) renderMap.getSizeRaster() / 2;
		float offset = (float) layer * (float) renderMap.getSizeRaster();

		materials.getMatGrid().setGLMaterial(gl);
		
		gl.glPushMatrix();

		for (int i = 0; i < renderMap.getSizeX() + 1; i++) {
			gl.glBegin(GL2.GL_LINES);

			gl.glVertex3f(-halfSizeRaster + i * (float) renderMap.getSizeRaster(), -halfSizeRaster + 0.002f + offset,
					-halfSizeRaster);
			gl.glVertex3f(-halfSizeRaster + i * (float) renderMap.getSizeRaster(), -halfSizeRaster + 0.002f + offset,
					(float) renderMap.getSizeRaster() * (float) renderMap.getSizeY() - halfSizeRaster);

			gl.glEnd();
		}

		for (int i = 0; i < renderMap.getSizeY() + 1; i++) {
			gl.glBegin(GL2.GL_LINES);

			gl.glVertex3f(-halfSizeRaster, -halfSizeRaster + 0.002f + offset,
					-halfSizeRaster + i * (float) renderMap.getSizeRaster());
			gl.glVertex3f((float) renderMap.getSizeRaster() * (float) renderMap.getSizeX() - halfSizeRaster,
					-halfSizeRaster + 0.002f + offset, -halfSizeRaster + i * (float) renderMap.getSizeRaster());

			gl.glEnd();
		}
		gl.glPopMatrix();

	}

	////////////////////////
	// rysowanie przeszkód
	public void drawObsticles(GL2 gl) {
		GLUT glut = new GLUT();

		materials.getMatObstacle().setGLMaterial(gl);
		for (int index = 0; index < renderMap.getObstacleShift().size(); index++) {
			materials.getMatObstacle().setGLDiffuse(gl);
			// kostka o okreœlonym materiale
			Point3d translate = renderMap.getObstacleShift().get(index);
			Point3d raster = renderMap.pointFromShift(renderMap.getObstacleShift(), index);
			if (this.isMapCreation && (int) raster.getZ() > offsetLayer)
				continue;
			if (this.isMapCreation && (int) raster.getZ() == offsetLayer)
				materials.getMatObstacleMod().setGLDiffuse(gl);
				
			gl.glPushMatrix();
			gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());

			glut.glutSolidCube((float) renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
	}

	/////////////////////////////////
	// rysowanie punktu start i stop
	public void drawStratEnd(GL2 gl) {
		GLUT glut = new GLUT();

		if (renderMap.isStart()) {
			materials.getMatStart().setGLDiffuse(gl);
			Point3d translate = renderMap.getStartShift();
			Point3d raster = renderMap.pointFromShift(renderMap.getStartShift());
			if (!(this.isMapCreation && (int) raster.getZ() > offsetLayer)) {
				gl.glPushMatrix();
				gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());
				glut.glutSolidCube((float) renderMap.getSizeRaster());
				gl.glPopMatrix();
			}
		}

		if (renderMap.isEnd()) {
			materials.getMatEnd().setGLDiffuse(gl);
			Point3d translate = renderMap.getEndShift();
			Point3d raster = renderMap.pointFromShift(renderMap.getEndShift());
			if (!(this.isMapCreation && (int) raster.getZ() > offsetLayer)) {
				gl.glPushMatrix();
				gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());
				glut.glutSolidCube((float) renderMap.getSizeRaster());
				gl.glPopMatrix();
			}
		}

	}

	////////////////////////
	// rysowanie œcie¿ki
	public void drawPath(GL2 gl) {
		GLUT glut = new GLUT();

		materials.getMatPath().setGLDiffuse(gl);

		for (int index = 0; index < renderMap.getPathShift().size(); index++) {
			// kostka o okreœlonym materiale
			Point3d translate = renderMap.getPathShift().get(index);

			gl.glPushMatrix();
			gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());
			glut.glutSolidCube((float) renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
	}

	///////////////////////////////////////////////
	/// Algorytm AStar
	
	////////////////////////
	// rysowanie zamkniete
	public void drawClosed(GL2 gl) {
		GLUT glut = new GLUT();

		materials.getMatClosed().setGLDiffuse(gl);

		for (int index = 0; index < renderMap.getClosedShift().size(); index++) {
			// kostka o okreœlonym materiale
			Point3d translate = renderMap.getClosedShift().get(index);

			gl.glPushMatrix();
			gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());
			glut.glutSolidCube((float) renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
	}

	////////////////////////
	// rysowanie otwarte
	public void drawOpen(GL2 gl) {
		GLUT glut = new GLUT();

		materials.getMatOpen().setGLDiffuse(gl);

		for (int index = 0; index < renderMap.getOpenShift().size(); index++) {
			// kostka o okreœlonym materiale
			Point3d translate = renderMap.getOpenShift().get(index);

			gl.glPushMatrix();
			gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());
			glut.glutSolidCube((float) renderMap.getSizeRaster());
			gl.glPopMatrix();
		}
	}

	/////////////////////////////////////////////
	// rysowanie aktualn¹ przetwarzany raster
	public void drawActual(GL2 gl) {
		GLUT glut = new GLUT();

		materials.getMatActual().setGLDiffuse(gl);

		// kostka o okreœlonym materiale
		Point3d translate = renderMap.getActualShift();

		gl.glPushMatrix();
		gl.glTranslatef((float) translate.getX(), (float) translate.getY(), (float) translate.getZ());
		glut.glutSolidCube((float) renderMap.getSizeRaster());
		gl.glPopMatrix();
		
	}

	// tworzenie screenshot funkcji
	public BufferedImage makeScreenshot() {
		int width = glcanvas.getWidth();
		int height = glcanvas.getHeight();

		BufferedImage screenshot = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = screenshot.getGraphics();

		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				graphics.setColor(new Color((buffer.get() & 0xff), (buffer.get() & 0xff), (buffer.get() & 0xff)));
				buffer.get();
				graphics.drawRect(w, height - h, 1, 1);
			}
		}

		return screenshot;
	}

	public void saveImage(String pathFile) {
		try {

			BufferedImage screenshot = makeScreenshot();

			ImageIO.write(screenshot, "PNG", new File(pathFile));
		} catch (IOException ex) {
			ex.printStackTrace();
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

	public MapEdition getEditionMap() {
		return editionMap;
	}

	public void setEditionMap(MapEdition editionMap) {
		this.editionMap = editionMap;
	}

	public boolean isAnimation() {
		return isAnimation;
	}

	public void setAnimation(boolean isAnimation) {
		this.isAnimation = isAnimation;
		this.renderMap.setAnimation(isAnimation);
	}

	public boolean isAStar() {
		return isAStar;
	}

	public void setAStar(boolean isAStar) {
		this.isAStar = isAStar;
	}

	public void setWindow(WindowMain window) {
		this.window = window;
	}

	public MaterialsList getMaterials() {
		return materials;
	}

	public void setMaterials(MaterialsList materials) {
		this.materials = materials;
	}
	
	//////////////////

}
