package optimumPath.opengl;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import optimumPath.common.*;
import optimumPath.object.Map;

public class MapEdition implements MouseListener, MouseMotionListener{

	private Point3d nearv, farv;
	private int sx, sy;
	private int selectedOption;
	private boolean isClicked, isDragged;
    private int mouseButton;

	
	
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

	public int getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(int selectedOption) {
		this.selectedOption = selectedOption;
	}

	public int getMouseButton() {
		return mouseButton;
	}

	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
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
        double y = (layer+1)*sizeRaster - halfSizeRaster;
        double t = (nearv.getY() - y) / (nearv.getY() - farv.getY());

        // so here are the desired (x, y) coordinates
        double x = nearv.getX() + (farv.getX() - nearv.getX()) * t;
        double z = nearv.getZ() + (farv.getZ() - nearv.getZ()) * t;
        System.out.println("World coords (" + x + ", " + 0.0 + ", " + z + ")");
        
        x = Math.floor((x + halfSizeRaster)/sizeRaster);
        y = Math.floor((y + halfSizeRaster)/sizeRaster - 1);
        z = Math.floor((z + halfSizeRaster)/sizeRaster);
        System.out.println("Map coords (" + x + ", " + 0.0 + ", " + z + ")");
        
        return new Point3d(x, z, y);
	}
	
	
	public void modifcationMap(Map renderMap, int layer) {
		Point3d raster = getPointRaster(renderMap.getSizeRaster(), layer);
		
		if(!renderMap.isPointInMap(raster))
			return;
		
		int x = (int)raster.getX();
		int y = (int)raster.getY();
		int z = (int)raster.getZ();
		
		switch(selectedOption) {
			case 0:
				if(renderMap.getRaster(x, y, z) == Raster.EMPTY)
					renderMap.setRaster(x, y, z, Raster.OBSTACLE);
				break;
			
			case 1:
				if(renderMap.getRaster(x, y, z) == Raster.OBSTACLE) {
					renderMap.setRaster(x, y, z, Raster.EMPTY);
					renderMap.removeFromlist(renderMap.getObstacleShift(), x, y, z);
				}
				break;
				
			case 2:
				if(renderMap.getRaster(x, y, z) == Raster.START) {
					if (!isDragged) {
						renderMap.setRaster(x, y, z, Raster.EMPTY);
						renderMap.setStart(false);
					}
				} else
					if(renderMap.getRaster(x, y, z) == Raster.EMPTY)
						renderMap.setRaster(x, y, z, Raster.START);
				break;
				
			case 3:
				if(renderMap.getRaster(x, y, z) == Raster.END) {
					if (!isDragged) {
						renderMap.setRaster(x, y, z, Raster.EMPTY);
						renderMap.setEnd(false);
					}
				} else
					if(renderMap.getRaster(x, y, z) == Raster.EMPTY)
						renderMap.setRaster(x, y, z, Raster.END);
				break;
		}
		
		isClicked = false;
	}
	
	///////////////////////////////////////////
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseButton = e.getButton();
		isClicked = true;
		sx = e.getX();
		sy = e.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		isClicked = false;
		isDragged = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		isClicked = true;
		isDragged = true;
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
