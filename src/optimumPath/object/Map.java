package optimumPath.object;

import java.util.ArrayList;
import java.util.List;

import optimumPath.common.*;

public class Map {

	public enum Raster {
	    EMPTY, OBSTACLE, START, END, PATH, FORBIDDEN;
	}
	
	private Raster rasterMap[][][];
	private int sizeX, sizeY, sizeZ;
	private double sizeRaster;
	
	private List<Point3d> obstacleShift;
	private List<Point3d> pathShift;
	private Point3d startShift;
	private Point3d endShift;
	
	private boolean isStart, isEnd;
	
	////////////////////////////////////
	// konstruktory klasy Map

	public Map() {
		sizeX = 10;
		sizeY = 10;
		sizeZ = 10;
		sizeRaster = 1.0;
		
		obstacleShift = new ArrayList<Point3d>();
		pathShift = new ArrayList<Point3d>();
		startShift = new Point3d();
		endShift = new Point3d();
		
		initMap();
	}
	
	public Map(int sizeX, int sizeY, int sizeZ, double sizeRaster) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.sizeRaster = sizeRaster;
		
		obstacleShift = new ArrayList<Point3d>();
		pathShift = new ArrayList<Point3d>();
		startShift = new Point3d();
		endShift = new Point3d();
		
		initMap();
	}
	
	/////////////////////////////////////////
	
	public void initMap() {
		rasterMap = new Raster[sizeZ][sizeY][sizeX];
		clearMap();
	}
	
	public void clearMap() {
		
		// czyszczenie mapy
		for(int z = 0; z < sizeZ; z++) {
			for(int y = 0; y < sizeY; y++) {
				for(int x = 0; x < sizeX; x++) {
					rasterMap[z][y][x] = Raster.EMPTY;
				}
			}
		}
		
		obstacleShift.clear();
		pathShift.clear();
		
		isStart = false;
		isEnd = false;
	}
	
	public void makeShiftList() {
		// ustawienie przesuniêæ dla mapy
		obstacleShift.clear();
		pathShift.clear();
		
		isStart = false;
		isEnd = false;
		
		for(int z = 0; z < sizeZ; z++) {
			for(int y = 0; y < sizeY; y++) {
				for(int x = 0; x < sizeX; x++) {
					makeShift(x, y, z);
				}
			}
		}
	}
	
	public int[][][] rasterMapToIntMap(){
		int intMap[][][] = new int[sizeZ][sizeY][sizeX];
		
		for(int z = 0; z < sizeZ; z++) {
			for(int y = 0; y < sizeY; y++) {
				for(int x = 0; x < sizeX; x++) {
					if(rasterMap[z][y][x] == Raster.OBSTACLE)
						intMap[z][y][x] = 1;
					else
						intMap[z][y][x] = 0;
				}
			}
		}
		
		return intMap;
	}
	
	public void intMapToRasterMap(int intMap[][][], int sizeX, int sizeY, int sizeZ){
		setSize(sizeX, sizeY, sizeZ);
		initMap();
		
		for(int z = 0; z < sizeZ; z++) {
			for(int y = 0; y < sizeY; y++) {
				for(int x = 0; x < sizeX; x++) {
					if(intMap[z][y][x] == 1)
						rasterMap[z][y][x] = Raster.OBSTACLE;
				}
			}
		}
		
		makeShiftList();
	}
	
	public Point3d pointFromShift(List<Point3d> List, int index) {
		Point3d point = List.get(index);
		double x = point.getX()/(double)sizeRaster;
		double y = point.getZ()/(double)sizeRaster;
		double z = point.getY()/(double)sizeRaster;
		return new Point3d(x, y, z);
	}
	
	public void makeShift(int x, int y, int z) {
		double shiftX = (double)x * sizeRaster;
		double shiftY = (double)z * sizeRaster;
		double shiftZ = (double)y * sizeRaster;
		
		switch (rasterMap[z][y][x]) {
			case OBSTACLE:
				obstacleShift.add(new Point3d(shiftX, shiftY, shiftZ));
				break;
				
			case PATH:
				pathShift.add(new Point3d(shiftX, shiftY, shiftZ));
				break;
				
			case START:
				startShift.setPoint(shiftX, shiftY, shiftZ);
				isStart = true;
				break;
				
			case END:
				endShift.setPoint(shiftX, shiftY, shiftZ);
				isEnd = true;
				break;
				
			default:
				break;
		}
	}
	
	public void removeFromlist(ArrayList<Point3d> List, int x, int y, int z) {
		double shiftX = (double)x * sizeRaster;
		double shiftY = (double)z * sizeRaster;
		double shiftZ = (double)y * sizeRaster;
		
		for (int index = 0; index < List.size(); index++) {
			if (List.get(index).equalsPoint(shiftX, shiftY, shiftZ))
				List.remove(index);
		}
	}
	
	public boolean isPointInMap(Point3d point) {
		boolean inRangeX = point.getX() >= 0 && point.getX() < sizeX;
		boolean inRangeY = point.getY() >= 0 && point.getY() < sizeY;
		boolean inRangeZ = point.getZ() >= 0 && point.getZ() < sizeZ;
		
		if (inRangeX && inRangeY && inRangeZ)
			return true;
		return false;
	}
	
	////////////////////////////////////
	// getters and setters
	
	public void setRaster(int x, int y, int z, Raster type) {
		if (type == rasterMap[z][y][x])
			return;
		
		this.rasterMap[z][y][x] = type;
		makeShift(x, y, z);
	}
	
	public Raster getRaster(int x, int y, int z) {
		return rasterMap[z][y][x];
	}
	
	
//	public Point3d getObstacleShift(int index) {
//		return obstacleShift.get(index);
//	}
//	
//	public void setObstacleShift(int index, double x, double y, double z) {
//		this.obstacleShift.get(index).setPoint(x, y, z);
//	}
//	
//	public Point3d getPathShift(int index) {
//		return pathShift.get(index);
//	}
//	
//	public void setPathShift(int index, double x, double y, double z) {
//		this.pathShift.get(index).setPoint(x, y, z);
//	}
	
	public Point3d getStartShift() {
		return startShift;
	}

	public void setStartShift(double x, double y, double z) {
		this.startShift.setPoint(x, y, z);
	}

	public Point3d getEndShift() {
		return endShift;
	}

	public void setEndShift(double x, double y, double z) {
		this.endShift.setPoint(x, y, z);
	}
	
	public Raster[][][] getRasterMap() {
		return rasterMap;
	}

	public void setRasterMap(Raster[][][] rasterMap) {
		this.rasterMap = rasterMap;
	}

	public List<Point3d> getObstacleShift() {
		return obstacleShift;
	}

	public List<Point3d> getPathShift() {
		return pathShift;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getSizeZ() {
		return sizeZ;
	}

	public void setSizeZ(int sizeZ) {
		this.sizeZ = sizeZ;
	}
	
	public Point3d getSize() {
		return new Point3d(this.sizeX, this.sizeY, this.sizeZ);
	}
	
	public void setSize(int sizeX, int sizeY, int sizeZ) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}

	public double getSizeRaster() {
		return sizeRaster;
	}

	public void setSizeRaster(double sizeRaster) {
		this.sizeRaster = sizeRaster;
	}
	
	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	/////////////////////////////////////////////
	
}
