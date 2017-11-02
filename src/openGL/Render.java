/**
 * 
 */
package openGL;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
/**
 * @author 
 *
 */
public class Render implements MouseListener, MouseMotionListener, GLEventListener{

	private double cameraAngleAboutX, cameraAngleAboutY;
    private double cameraCurX, cameraCurY, cameraCurZ;
    private double cameraDefaultX, cameraDefaultY, cameraDefaultZ;
    private double lookAtX, lookAtY, lookAtZ;
    private double upX, upY, upZ;
    
    private int prevMouseX, prevMouseY;
    private int mouseButton;
    
    
	public static DisplayMode dm, dm_old;
	private GLU glu = new GLU();
	
	public Render()
	{
		cameraAngleAboutX = 0.0;
		cameraAngleAboutY = 0.0;
		
		cameraDefaultX = 0.0;
		cameraDefaultY = 0.0;
		cameraDefaultZ = 5.0;

		cameraCurX = cameraDefaultX;
		cameraCurY = cameraDefaultY;
		cameraCurZ = cameraDefaultZ;

		lookAtX = 0.0;
		lookAtY = 0.0;
		lookAtZ = 0.0;

		upX = 0.0;
		upY = 1.0;
		upZ = 0.0;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		
		// method body 
		float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float mat_ambient_color[] = { 0.8f, 0.8f, 0.2f, 1.0f };
		float mat_diffuse1[] = { 0.6f, 0.5f, 0.8f, 1.0f };
		float mat_diffuse2[] = { 0.1f, 0.5f, 0.8f, 1.0f };
		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float low_shininess[] = { 5.0f };
		
		final GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		
		// Rotate The Cube On X, Y & Z
		gl.glRotatef(0.0f, 1.0f, 1.0f, 1.0f);
		
		//rysowanie podstawy
		float size_flor = 12.0f/2;
		gl.glPushMatrix(); 
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse1, 0);
		gl.glBegin( GL2.GL_QUADS );
		gl.glVertex3f( size_flor, -2.0f, size_flor ); 
	    gl.glVertex3f( -size_flor, -2.0f, size_flor ); 
	    gl.glVertex3f( -size_flor, -2.0f, -size_flor ); 
	    gl.glVertex3f( size_flor, -2.0f, -size_flor );  
	    gl.glEnd();
		gl.glPopMatrix();
		////////////////
		
		//kostka o okreœlonym materiale
		gl.glPushMatrix();
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_ambient_color, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse2, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, low_shininess, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, no_mat, 0);
		glut.glutSolidCube(2);
		gl.glPopMatrix();
		////////////////
		
		gl.glLoadIdentity();
		gl.glTranslatef( 0.0f, 0.0f, -5.0f ); 
		glu.gluLookAt(cameraCurX, cameraCurY, cameraCurZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ );
		
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
		gl.glDepthFunc( GL2.GL_LESS );
		gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
		
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
				
		glu.gluPerspective( 45.0f, h, 1.0, 40.0 );
		gl.glMatrixMode( GL2.GL_MODELVIEW );
		gl.glLoadIdentity();
	}
		

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();		// akualna pozycja myszki
		int y = e.getY();
		Dimension size = e.getComponent().getSize();  // rozmiar canvas

		double mouseDeltaXInDegrees = 360.0 * ( (double)(prevMouseX-x) / (double)size.width);
		double mouseDeltaYInDegrees = 360.0 * ( (double)(y-prevMouseY) / (double)size.height);
		double mouseDeltaYInScreenPercent = ( (double)(prevMouseY-y) / (double)size.width);

		if (mouseButton == 1) {
			cameraAngleAboutY += mouseDeltaXInDegrees;
			cameraAngleAboutX += mouseDeltaYInDegrees;
		}
		if (mouseButton == 2) {
			lookAtX += (double)(prevMouseX-x)*3.0 / (double)size.width;
			lookAtY += (double)(prevMouseY-y)*3.0 / (double)size.height;
		}
		if (mouseButton == 3)
			cameraDefaultZ = cameraDefaultZ - (mouseDeltaYInScreenPercent*cameraDefaultZ);
		
		cameraCurY = cameraDefaultZ * (double)Math.sin(Math.toRadians(cameraAngleAboutX));
		cameraCurX = cameraDefaultZ * (double)Math.sin(Math.toRadians(cameraAngleAboutY));
		cameraCurZ = cameraDefaultZ * (double)Math.cos(Math.toRadians(cameraAngleAboutY));
		
		prevMouseX = x;		// zapisuje aktualn¹ pozycje do prevMouse
		prevMouseY = y;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseButton = e.getButton();	// naciœniêty przycisk
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
		
	}
