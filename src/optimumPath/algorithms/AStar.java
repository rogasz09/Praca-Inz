package optimumPath.algorithms;

import optimumPath.common.*;

public class AStar {

	private Raster map[][][];
	int sizeX;
	int sizeY;
	int sizeZ;
	
	private Node startNode;
	private Node endNode;
		

	
	AStar(Raster[][][] inputMap, int inputSizeZ, int inputSizeY, int inputSizeX){
		initMap(inputMap, inputSizeZ, inputSizeZ, inputSizeZ);
	}
	
	private void initMap(Raster[][][] inputMap,int inputSizeZ, int inputSizeY, int inputSizeX) {
		this.map = inputMap;
		this.sizeZ = inputSizeZ;
		this.sizeY = inputSizeY;
		this.sizeX = inputSizeX;
		
		
		// I madethis initialization, in case to avoid problems with no start or end point
		startNode = new Node(0, 0, 0, Raster.START);
		endNode = new Node(1, 1, 1, Raster.END);
		
		for (int z = 0; z < sizeZ; z++) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					if(map[z][y][x] == Raster.START) {
						startNode = new Node(z, y, x, Raster.START);
					}
					if(map[z][y][x] == Raster.END) {
						endNode = new Node(z, y, x, Raster.END);
					}
						
				}
			}
		}
	}
	
	private void printMap() {
		for (int z = 0; z < sizeZ; z++) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					System.out.println(map[z][y][x]);
				}
			}
		}
	}
	
	
	public Raster[][][] getMap() {
		return map;
	}

	public void setMap(Raster[][][] map) {
		this.map = map;
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

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}
	
}
