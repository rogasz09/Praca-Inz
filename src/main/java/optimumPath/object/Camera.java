package optimumPath.object;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import optimumPath.common.Point3d;

public class Camera implements MouseListener, MouseMotionListener, MouseWheelListener {
    
    private Point3d pointPos, pointCenter, vectorUp; // punkt Pos, punkt Center, vektor Up
    private Point3d viewVector;
    //private double cameraSpeed = 0.1; // szybkosc kamery
    
    private Point3d prevPointPos, prevPointCenter, prevVectorUp; // poprzednie ustawienia kamery (przed modyfikacja mapy)
    private int prevMouseX, prevMouseY;
    private int mouseButton;
    
    private double lengthViewVector;
    private double alfa, beta;
    private double prevAlfa, prevBeta;
    
    private boolean isMapCreation = false;
    
    private MouseEvent mouse;
    
    
    //////////////////////////////
    // konstruktory
    //////////////////////////////
    
    public Camera() {
    	pointPos	= new Point3d();
    	pointCenter	= new Point3d();
    	vectorUp	= new Point3d(0.0, 1.0, 0.0);
    	
    	viewVector = new Point3d();
    	calculateViewVector();
    	calculateAlfaBeta();
    }

    public Camera(double posX,    double posY,    double posZ,
    			  double centerX, double centerY, double centerZ,
    			  double upX,     double upY,     double upZ) 
    {
    	pointPos	= new Point3d(posX, posY, posZ);
    	pointCenter	= new Point3d(centerX, centerY, centerZ);
    	vectorUp	= new Point3d(upX, upY, upZ);
    	
    	viewVector = new Point3d();
    	calculateViewVector();
    	calculateAlfaBeta();
    }
    
    
    //////////////////////////////////////
    
    // oblicza wektor view oraz jego dlugosc
    public void calculateViewVector() {
    	viewVector.setPoint(pointCenter); 
    	viewVector.subPoint(pointPos);
    	lengthViewVector = lengthVector(pointCenter, pointPos);
    	//System.out.println(lengthViewVector);
    }
    
    // oblicza wartosci alfa beta
    public void calculateAlfaBeta() {
    	Point3d mPos = new Point3d(pointPos.getX(), 0, pointPos.getZ());
    	Point3d mCent = new Point3d(pointCenter.getX(), 0, pointCenter.getZ());
    	double R = lengthVector(mCent, mPos);
    	
    	alfa = Math.acos((pointPos.getX() - pointCenter.getX())/R);
    	beta = Math.acos(R/lengthViewVector);
    	System.out.println(alfa);
    	System.out.println(beta);
    }
    
    // liczenie dlugosci wektora
    public double lengthVector(Point3d point1, Point3d point2) {
    	double x = point1.getX() - point2.getX();
    	double y = point1.getY() - point2.getY();
    	double z = point1.getZ() - point2.getZ();
    	return Math.sqrt(x*x + y*y + z*z);
    }
    
    // zapsisz aktualna pozycje kamery
    public void saveActualCamera() {
    	// zapsianie punktow kamery
    	prevPointPos = pointPos;
		prevPointCenter = pointCenter;
		prevVectorUp = vectorUp;
		// zapsianie alfa i beta
		prevAlfa = alfa;
		prevBeta = beta;
    }
    
    // wczytaj poprzednia pozycje kamery
    public void loadPrevCamera() {
    	//sprawdza czy jest zapisana kamera
    	if (prevPointPos == null || prevPointCenter == null || prevVectorUp == null)
    		return;
    	// wczytanie punktow kamery
    	pointPos = prevPointPos;
		pointCenter = prevPointCenter;
		vectorUp = prevVectorUp;
		// wczytanie alfa i beta
		alfa = prevAlfa;
		beta = prevBeta;
    }
    
    
    //czyszczenie pamieci starej kamery
    public void clearPrevCamera() {
    	// czyszcenie punktow kamery
    	prevPointPos = null;
		prevPointCenter = null;
		prevVectorUp = null;
		// czyszcenia alfa i beta
		prevAlfa = 0.0;
		prevBeta = 0.0;
    }
    
    //sprawdza czy sa zapisane ustawienia kamery
    public boolean isPrevCamera() {
    	if (prevPointPos == null && prevPointCenter == null && prevVectorUp == null)
    		return false;
    	return true;
    }
    
    ///////////////////////////////////////////
    // Obs³uga kamery
    ///////////////////////////////////////////
    
    
    // obrót kamery wokó³ osi Y i Z
    public void rotateView(int shiftX, int shiftY, Dimension size) {
    	double dAlfa  = 0.0;				
    	double dBeta  = 0.0;	

    	dAlfa = -1.5 * (double)shiftX / size.getWidth();	// k¹t obrotu wokó³ osi Y
    	dBeta = -(double)shiftY / size.getHeight(); // k¹t obrotu wokó³ osi Z
    	calculateViewVector();
    	
    	//nowy kat alfa i beta
    	alfa += dAlfa;
    	beta += dBeta;
    	
    	//ustawienie zakresu kata alfa (0deg, 360deg)
    	if (alfa > 2*Math.PI)
    		alfa -= 2*Math.PI;
    	if (alfa < 0)
    		alfa += 2*Math.PI;
    	
    	//ustawienie zakresu kata beta (-90deg, 90deg)
    	if (beta > Math.PI/2-0.05 || beta < -Math.PI/2+0.05)
    		beta -= dBeta;
    	
    	// obrót kamery
    	pointPos.setX(pointCenter.getX() + lengthViewVector*Math.abs(Math.cos(beta))*Math.cos(alfa));
    	pointPos.setZ(pointCenter.getZ() + lengthViewVector*Math.abs(Math.cos(beta))*Math.sin(alfa));
    	pointPos.setY(pointCenter.getY() + lengthViewVector*Math.sin(beta));
    	//pointCenter.setZ(pointPos.getZ() + Math.sin(-angleY)*viewVector.getX() + Math.cos(-angleY)*viewVector.getZ());
    	//pointCenter.setX(pointPos.getX() + Math.cos(-angleY)*viewVector.getX() - Math.sin(-angleY)*viewVector.getZ());
    	//pointCenter.setY(pointCenter.getY() + angleZ * 2);
    }
    
    // przyblizenie i oddalenie kamery
    public void zoomView(int shiftMouse, Dimension size) {
    	calculateViewVector();
    	//calculateAlfaBeta();
    	
    	double speedZoom = (double)shiftMouse / size.getWidth(); // okreœla szybkoœæ przesuniêcia 
    	
    	// przesuniecie kamery do przodu lub do ty³u
    	pointPos.setX(pointPos.getX()  + viewVector.getX() * speedZoom);
    	pointPos.setY(pointPos.getY()  + viewVector.getY() * speedZoom);
    	pointPos.setZ(pointPos.getZ()  + viewVector.getZ() * speedZoom);
    	//pointCenter.setX(pointCenter.getX() + viewVector.getX() * speedZoom);
    	//pointCenter.setZ(pointCenter.getZ() + viewVector.getZ() * speedZoom);
    	//pointPos.setZ(pointPos.getZ() - (speedZoom*pointPos.getZ()));
    }
    
    // przesuniêcie kamery
    public void shiftView(int shiftX, int shiftY, Dimension size) {
    	calculateViewVector();
    	
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
    	
    	calculateViewVector();
		calculateAlfaBeta();
    }
    

	public Point3d getPointPos() {
		return pointPos;
	}

	public void setPointPos(Point3d pointPos) {
		this.pointPos = pointPos;
		calculateViewVector();
		calculateAlfaBeta();
	}

	public Point3d getPointCenter() {
		return pointCenter;
	}

	public void setPointCenter(Point3d pointCenter) {
		this.pointCenter = pointCenter;
		calculateViewVector();
		calculateAlfaBeta();
	}

	public Point3d getVectorUp() {
		return vectorUp;
	}

	public void setVectorUp(Point3d vectorUp) {
		this.vectorUp = vectorUp;
	}
	
	public boolean isMapCreation() {
		return isMapCreation;
	}

	public void setMapCreation(boolean isMapCreation) {
		this.isMapCreation = isMapCreation;
	}
	
	public MouseEvent getMouse() {
		return mouse;
	}

	public void setMouse(MouseEvent mouse) {
		this.mouse = mouse;
	}
    
    /////////////////////////////////////////
	
	
	
    //////////////////////////////////////
	// Obs³uga myszy 
    //////////////////////////////////////
	

	@Override
	public void mousePressed(MouseEvent e) {
		mouse = e;
		mouseButton = e.getButton();	// naciœniêty przycisk
		prevMouseX = e.getX();  // zapisanie pozycji x myszy
		prevMouseY = e.getY();  // zapisanie pozycji y myszy
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		int shiftX = prevMouseX - e.getX();
		int shiftY = prevMouseY - e.getY();
		Dimension size = e.getComponent().getSize();
		//System.out.println("D³ugoœc wektora view: " + lengthViewVector);
		//System.out.println("Alfa: " + alfa*180/Math.PI);
		//System.out.println("Beta: " + beta*180/Math.PI);
		
		if (mouseButton == 1 && !isMapCreation) {
			// lewy przycisk myszy
			// obrót kamery
			rotateView(shiftX, shiftY, size);
		}
//		if (mouseButton == 2 && !isMapCreation) {
//			// œrodkowy przycisk myszy
//			// przesuniêcie kamery
//			shiftView(-shiftX, shiftY, size);
//		}
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
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Dimension size = e.getComponent().getSize();
        if (e.getWheelRotation() < 0)
        	zoomView(25, size);
        else
        	zoomView(-25, size);
	}
	
 //////////////////////////////////////////////
	
	
	///////////////////////////////////////////
	// Niewykorzystane metody
	
	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
