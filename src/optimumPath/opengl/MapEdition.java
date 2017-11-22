package optimumPath.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import optimumPath.common.Point3d;

public class MapEdition implements MouseListener, MouseMotionListener{

	private Point3d nearv, farv;
	private int sx, sy;
	private boolean isClicked;
	
	
	//////////////////////////////
	// konstruktory
	//////////////////////////////
	
	public MapEdition() {
		nearv = new Point3d();
		farv = new Point3d();
	}
	
	//////////////////////////////////
	

	/////////////////////////////
	// ------ getty i setty----
	
	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public void getCoordinatesFromCanvas(GL2 gl, GLU glu) {
		int realy = 0;// GL y coord pos
		
		int viewport[] = new int[4];
	    double mvmatrix[] = new double[16];
	    double projmatrix[] = new double[16];
	    double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords
		
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
        
        realy = viewport[3] - (int) sy - 1;
        System.out.println(realy);
        glu.gluUnProject((double) sx, (double) realy, 0.0, mvmatrix, 0,
            projmatrix, 0, 
            viewport, 0, 
            wcoord, 0);
        nearv.setPoint(wcoord[0], wcoord[1], wcoord[2]);
        
        glu.gluUnProject((double) sx, (double) realy, 1.0, //
            mvmatrix, 0,
            projmatrix, 0,
            viewport, 0, 
            wcoord, 0);
        farv.setPoint(wcoord[0], wcoord[1], wcoord[2]);
        
        //isClicked = false;
	}
	
	public Point3d getPointRaster(double sizeRaster, int layer) {
		if(nearv.getY() == farv.getY())     // this means we have no solutions
			return new Point3d();
		
        double halfSizeRaster = sizeRaster/ 2;
        double y = layer*sizeRaster - halfSizeRaster;
        double t = (nearv.getY() - y) / (nearv.getY() - farv.getY());

        // so here are the desired (x, y) coordinates
        double x = nearv.getX() + (farv.getX() - nearv.getX()) * t;
        double z = nearv.getZ() + (farv.getZ() - nearv.getZ()) * t;
        System.out.println("World coords (" + x + ", " + 0.0 + ", " + z + ")");
        
        x = Math.floor((x + halfSizeRaster)/sizeRaster);
        y = Math.floor((y + halfSizeRaster)/sizeRaster);
        z = Math.floor((z + halfSizeRaster)/sizeRaster);
        System.out.println("Map coords (" + x + ", " + 0.0 + ", " + z + ")");
        
        return new Point3d(x, z, y);
	}
	
	///////////////////////////////////////////
	
	@Override
	public void mousePressed(MouseEvent e) {
		isClicked = true;
		sx = e.getX();
		sy = e.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		isClicked = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		sx = e.getX();
		sy = e.getY();
	}

	///////////////////////////////////////////
	// Niewykorzystane metody

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

}
