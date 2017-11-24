package optimumPath.algorithms;

import java.util.ArrayList;

import optimumPath.common.*;

public class AStar {

	private Raster map[][][];
	int sizeX;
	int sizeY;
	int sizeZ;

	private Node startNode;
	private Node endNode;

	AStar(Raster[][][] inputMap, int inputSizeZ, int inputSizeY, int inputSizeX) {
		initMap(inputMap, inputSizeZ, inputSizeZ, inputSizeZ);
	}

	private void initMap(Raster[][][] inputMap, int inputSizeZ, int inputSizeY, int inputSizeX) {

		this.sizeZ = inputSizeZ;
		this.sizeY = inputSizeY;
		this.sizeX = inputSizeX;
		this.map = new Raster[sizeZ][sizeY][sizeX];

		// I madethis initialization, in case to avoid problems with no start or end
		// point
		startNode = new Node(0, 0, 0, Raster.START);
		endNode = new Node(1, 1, 1, Raster.END);

		for (int z = 0; z < sizeZ; z++) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					map[z][y][x] = inputMap[z][y][x];

					if (map[z][y][x] == Raster.START) {
						startNode = new Node(z, y, x, Raster.START);
					}
					if (map[z][y][x] == Raster.END) {
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

	public void perform() {
		ArrayList<Node> closedset = new ArrayList<Node>();
		ArrayList<Node> openset = new ArrayList<Node>();
		ArrayList<Node> neighbours;
		
		double g = 0.0;
		double h = count_h_manhattan(startNode);
		double f = g + h;
		startNode.setGHF(g, h, f);
		
		openset.add(startNode);
		//openset.addAll(this.getNeighboursManhattan(startNode));
		int x;
		while (!openset.isEmpty()) {
			x = getLowestFCostIndex(openset);
			
			if (isSameNode(openset.get(x), endNode))
				return;
			
			
			neighbours = getNeighboursManhattan(openset.get(x));
			closedset.add(openset.get(x));
			openset.remove(x);
			
			for(int i = 0; i < neighbours.size(); i++) {
				if (isInSet(neighbours.get(i), closedset))
					continue;
				
				Node neighbour = neighbours.get(i);
				Node parent = closedset.get(closedset.size()-1);
				
				g = parent.getG() + 1.0;
				//h = count_h_manhattan(neighbour);
				//f = g + h;
				//neighbour.setGHF(g, h, f);
				
				boolean tentative_is_better = false;
				if (!isInSet(neighbour, openset)) {
					openset.add(neighbour);
					h = count_h_manhattan(neighbour);
					neighbour.setH(h);
					tentative_is_better = true;
				} else if(g < neighbour.getG())
					tentative_is_better = true;
				if(tentative_is_better == true) {
					neighbour.setParent(parent.getZ(), parent.getY(), parent.getX(), parent.getType());
					neighbour.setG(g);
					f = g + h;
					neighbour.setF(f);
				}
					
			}
		}

	}
	
	private boolean isInSet(Node currentNode, ArrayList<Node> list) {
		for(int i = 0; i < list.size(); i++) {
			if (isSameNode(list.get(i), currentNode))
				return true;
		}
		return false;
	}
	
	private boolean isSameNode(Node node1, Node node2) {
		if (node1.getZ() == node2.getZ())
			if (node1.getY() == node2.getY())
				if (node1.getX() == node2.getX())
					return true;
		
		return false;
	}
	
	private int getLowestFCostIndex(ArrayList<Node> openset) {
		double lowestFCost = openset.get(0).getF();
		int index = 0;
		
		for(int i = 1; i < openset.size(); i++) {
			if(openset.get(i).getF() < lowestFCost) {
				lowestFCost = openset.get(i).getF();
				index = i;
			}
		}
		return index;
	}
	
	private double count_h_manhattan(Node currentNode) {
		return Math.abs(currentNode.getZ() - endNode.getZ()) +
			   Math.abs(currentNode.getY() - endNode.getY()) +
			   Math.abs(currentNode.getX() - endNode.getX());
	}
	
	// check if a raster is in map and if it is not obstacle or forbidden
	private boolean checkNode(int z, int y, int x) {
		
		if(z < sizeZ && z>=0) {
			if(y < sizeY && y>=0) {
				if(x < sizeX && x>=0) {
			
					if(map[z][y][x] != Raster.FORBIDDEN &&
							map[z][y][x] !=	Raster.OBSTACLE	) {
						return true;
					}
				}
			}
		}
			return false;
	}
	
	private ArrayList<Node> possibleNeighbours(Node current) {
		int currentX = current.getX();
		int currentY = current.getY();
		int currentZ = current.getZ();
		
		ArrayList<Node> possibleNeighbours = new ArrayList<Node>();
		possibleNeighbours.add(new Node(currentZ, currentY, currentX + 1));
		possibleNeighbours.add(new Node(currentZ, currentY, currentX - 1));
		possibleNeighbours.add(new Node(currentZ, currentY + 1, currentX));
		possibleNeighbours.add(new Node(currentZ, currentY - 1, currentX));
		possibleNeighbours.add(new Node(currentZ + 1, currentY, currentX));
		possibleNeighbours.add(new Node(currentZ - 1, currentY, currentX));
		
		return possibleNeighbours;
	}

	private ArrayList<Node> getNeighboursManhattan(Node currentNode) {
		
		ArrayList<Node> neighbours = possibleNeighbours(currentNode);
		for(int i = 0; i < neighbours.size(); i++) {
			Node testNode = neighbours.get(i);
			if(!checkNode(testNode.getZ(), testNode.getY(), testNode.getX()))
				neighbours.remove(i);
		}

		return neighbours;
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
