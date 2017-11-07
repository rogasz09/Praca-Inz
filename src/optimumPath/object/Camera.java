package optimumPath.object;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import optimumPath.common.Point3d;

public class Camera implements MouseListener, MouseMotionListener {
    
    private Point3d pointPos, pointCenter, vectorUp; // punkt Pos, punkt Center, vektor Up
    //private double cameraSpeed = 0.1; // szybkosc kamery
    
    private int prevMouseX, prevMouseY;
    private int mouseButton;
    
    
    //////////////////////////////
    // konstruktory
    //////////////////////////////
    
    public Camera() {
    	pointPos	= new Point3d();
    	pointCenter	= new Point3d();
    	vectorUp	= new Point3d(0.0, 1.0, 0.0);
    }

    public Camera(double posX,    double posY,    double posZ,
    			  double centerX, double centerY, double centerZ,
    			  double upX,     double upY,     double upZ) 
    {
    	pointPos	= new Point3d(posX, posY, posZ);
    	pointCenter	= new Point3d(centerX, centerY, centerZ);
    	vectorUp	= new Point3d(upX, upY, upZ);
    }
    
    //////////////////////////////////////
    
    
    
    ///////////////////////////////////////////
    // Obs³uga kamery
    ///////////////////////////////////////////
    
    
    // obrót kamery wokó³ osi Y i Z
    public void rotateView(int shiftX, int shiftY, Dimension size) {
    	double angleY  = 0.0;				
    	double angleZ  = 0.0;	

    	angleY = 1.5 * (double)shiftX / size.getWidth();	// k¹t obrotu wokó³ osi Y
    	angleZ = (double)shiftY / size.getHeight(); // k¹t obrotu wokó³ osi Z
    	
    	Point3d viewVector = new Point3d(pointCenter); 
    	viewVector.subPoint(pointPos); // wektor widoku

    	// obrót kamery
    	pointCenter.setZ(pointPos.getZ() + Math.sin(-angleY)*viewVector.getX() + Math.cos(-angleY)*viewVector.getZ());
    	pointCenter.setX(pointPos.getX() + Math.cos(-angleY)*viewVector.getX() - Math.sin(-angleY)*viewVector.getZ());
    	pointCenter.setY(pointCenter.getY() + angleZ * 2);
    }
    
    // przyblizenie i oddalenie kamery
    public void zoomView(int shiftMouse, Dimension size) {
    	Point3d viewVector = new Point3d(pointCenter); 
    	viewVector.subPoint(pointPos); // wektor widoku
    	
    	double speedZoom = (double)shiftMouse / size.getWidth(); // okreœla szybkoœæ przesuniêcia 
    	
    	// przesuniecie kamery do przodu lub do ty³u
    	pointPos.setX(pointPos.getX()  + viewVector.getX() * speedZoom);
    	pointPos.setZ(pointPos.getZ()  + viewVector.getZ() * speedZoom);
    	pointCenter.setX(pointCenter.getX() + viewVector.getX() * speedZoom);
    	pointCenter.setZ(pointCenter.getZ() + viewVector.getZ() * speedZoom);
    	//pointPos.setZ(pointPos.getZ() - (speedZoom*pointPos.getZ()));
    }
    
    // przesuniêcie kamery
    public void shiftView(int shiftX, int shiftY, Dimension size) {
    	Point3d viewVector = new Point3d(pointCenter); 
    	viewVector.subPoint(pointPos); // wektor widoku
    	
    	Point3d viewOrthoVector = new Point3d(-viewVector.getZ(), 0.0, viewVector.getX()); // wektor ortogonalny dla wektora View w osi x
    	
    	double speedX = (double)shiftX / size.getWidth();	// szybkoœæ przesuniecia w osi x
    	double speedY = (double)shiftY / size.getHeight(); // szybkoœæ przesuniecia w osi y


    	// przsuniecie kamery lewo lub prawo
    	pointPos.setX(pointPos.getX()  + viewOrthoVector.getX() * speedX);
    	pointPos.setZ(pointPos.getZ()  + viewOrthoVector.getZ() * speedX);
    	pointCenter.setX(pointCenter.getX() + viewOrthoVector.getX() * speedX);
    	pointCenter.setZ(pointCenter.getZ() + viewOrthoVector.getZ() * speedX);
    	
    	// przsuniecie kamery góra lub dó³
    	pointPos.setY(pointPos.getY()  + 4*speedY);
    	pointCenter.setY(pointCenter.getY() + 4*speedY);
    }
    
    /////////////////////////////////////
    
    
    //////////////////////////////////////
	// getters and setters
    //////////////////////////////////////
    
    public void setPosition(double posX,    double posY,    double posZ,
    						double centerX, double centerY, double centerZ,
    						double upX,     double upY,     double upZ)
    {
    	pointPos.setPoint(posX, posY, posZ);
    	pointCenter.setPoint(centerX, centerY, centerZ);
    	vectorUp.setPoint(upX, upY, upZ);
    }
    

	public Point3d getPointPos() {
		return pointPos;
	}

	public void setPointPos(Point3d pointPos) {
		this.pointPos = pointPos;
	}

	public Point3d getPointCenter() {
		return pointCenter;
	}

	public void setPointCenter(Point3d pointCenter) {
		this.pointCenter = pointCenter;
	}

	public Point3d getVectorUp() {
		return vectorUp;
	}

	public void setVectorUp(Point3d vectorUp) {
		this.vectorUp = vectorUp;
	}
    
    /////////////////////////////////////////
	
	
	
    //////////////////////////////////////
	// Obs³uga myszy 
    //////////////////////////////////////
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseButton = e.getButton();	// naciœniêty przycisk
		prevMouseX = e.getX();  // zapisanie pozycji x myszy
		prevMouseY = e.getY();  // zapisanie pozycji y myszy
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		int shiftX = prevMouseX - e.getX();
		int shiftY = prevMouseY - e.getY();
		Dimension size = e.getComponent().getSize();
		
		if (mouseButton == 1) {
			// lewy przycisk myszy
			// obrót kamery
			rotateView(shiftX, shiftY, size);
		}
		if (mouseButton == 2) {
			// œrodkowy przycisk myszy
			// przesuniêcie kamery
			shiftView(-shiftX, shiftY, size);
		}
		if (mouseButton == 3) {
			// prawy przycisk myszy
			// przybli¿enie i oddalenie kamery
			int shiftMouse;
			if (shiftY > 0)
				shiftMouse = (int)Math.sqrt(Math.pow(shiftX, 2) + Math.pow(shiftY, 2));
			else
				shiftMouse = -(int)Math.sqrt(Math.pow(shiftX, 2) + Math.pow(shiftY, 2));
			
			zoomView(shiftMouse, size);
		}
		
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}
	
	
 //////////////////////////////////////////////
	
	
	///////////////////////////////////////////
	// Niewykorzystane metody
	
	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
