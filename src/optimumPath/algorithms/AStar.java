package optimumPath.algorithms;

import java.util.ArrayList;

import optimumPath.common.*;
import optimumPath.object.*;

public class AStar {

	private Raster map[][][];
	private Map renderMap;
	int sizeX;
	int sizeY;
	int sizeZ;

	private Node startNode;
	private Node endNode;

	private ArrayList<Node> path;

	public AStar(Map inputMap, int inputSizeZ, int inputSizeY, int inputSizeX) {
		initMap(inputMap, inputSizeZ, inputSizeY, inputSizeX);
	}

	public void initMap(Map inputMap, int inputSizeZ, int inputSizeY, int inputSizeX) {
		this.renderMap = inputMap;
		this.sizeZ = inputSizeZ;
		this.sizeY = inputSizeY;
		this.sizeX = inputSizeX;
		this.map = new Raster[sizeZ][sizeY][sizeX];

		// I madethis initialization, in case to avoid problems with no start or end
		// point
		startNode = new Node(0, 0, 0, Raster.START);
		endNode = new Node(1, 1, 1, Raster.END);

		// printMap(inputMap);

		for (int z = 0; z < sizeZ; z++) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					map[z][y][x] = inputMap.getRasterMap()[z][y][x];

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

	private void printMap(Raster[][][] inputMap) {
		for (int z = 0; z < sizeZ; z++) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					switch (inputMap[z][y][x]) {
					case START: {
						System.out.print("1 ");
						break;
					}
					case END: {
						System.out.print("2 ");
						break;
					}
					case OBSTACLE: {
						System.out.print("# ");
						break;
					}
					case FORBIDDEN: {
						System.out.print("@ ");
						break;
					}
					case EMPTY: {
						System.out.print("0 ");
						break;
					}
					default:
						System.out.print("0 ");
					}
				}
				System.out.print('\n');
			}
			System.out.print('\n');
		}
	}

	private void printList(ArrayList<Node> list) {
		System.out.print("List: ");
		for (int i = 0; i < list.size(); i++) {
			printNode(list.get(i));
		}
		System.out.print('\n');
	}

	private void printNode(Node node) {
		System.out.print("( ");
		System.out.print(Integer.toString(node.getX()) + " ");
		System.out.print(Integer.toString(node.getY()) + " ");
		System.out.print(Integer.toString(node.getZ()) + " ");
		System.out.print("), ");
	}

	public void writePathToMap(Raster[][][] rasterMap) {
		if (path == null)
			return;

		for (int i = 0; i < path.size(); i++) {
			Node currentNode = path.get(i);
			rasterMap[currentNode.getZ()][currentNode.getY()][currentNode.getX()] = Raster.PATH;
		}
	}
	
	private double getChebyshevCost(Node actualNode,Node neighbour) {
		int direction =  checkChebyshevDirection(actualNode, neighbour);
		return Math.sqrt((double)direction);
	}
	
	private int checkChebyshevDirection(Node actualNode,Node neighbour) {
		int sameX,sameY,sameZ;
		int sum = 0;
		sameX = (actualNode.getX()==neighbour.getX()) ? 0 : 1;
		sameY = (actualNode.getY()==neighbour.getY()) ? 0 : 1;
		sameZ = (actualNode.getZ()==neighbour.getZ()) ? 0 : 1;
		sum = sameX + sameY + sameZ;
		return sum;
	}

	private boolean checkPossibleTransition(Node actualNode,Node neighbour) {
		int direction =  checkChebyshevDirection(actualNode, neighbour);
		
		ArrayList<Node> neighboursActual = possibleNeighboursChebyshev(actualNode);
		ArrayList<Node> neighboursNeighbour = possibleNeighboursChebyshev(neighbour);
		ArrayList<Node> shared = commonPart(neighboursActual, neighboursNeighbour);
		
		if (direction == 3) {
			for (int i = 0; i < shared.size(); i++) {
				int x = shared.get(i).getX();
				int y = shared.get(i).getY();
				int z = shared.get(i).getZ();
				if (map[z][y][x] == Raster.FORBIDDEN || map[z][y][x] == Raster.OBSTACLE) {
					return false;
				} 
			}
			return true;
		}
		if (direction == 2) {
			ArrayList<Node> tmp = new ArrayList<Node>();
			if (actualNode.getX() == neighbour.getX()) {
				for (int i = 0; i < shared.size(); i++) {
					int x = shared.get(i).getX();
					int y = shared.get(i).getY();
					int z = shared.get(i).getZ();
					if(actualNode.getX() == x)
						if (map[z][y][x] == Raster.FORBIDDEN || map[z][y][x] == Raster.OBSTACLE) {
							return false;
					} else
						continue;
				}
				return true;
			} else if(actualNode.getY() == neighbour.getY()) {
				for (int i = 0; i < shared.size(); i++) {
					int x = shared.get(i).getX();
					int y = shared.get(i).getY();
					int z = shared.get(i).getZ();
					if(actualNode.getY() == y)
						if (map[z][y][x] == Raster.FORBIDDEN || map[z][y][x] == Raster.OBSTACLE) {
							return false;
					} else
						continue;
				}
				return true;
			} else {
				for (int i = 0; i < shared.size(); i++) {
					int x = shared.get(i).getX();
					int y = shared.get(i).getY();
					int z = shared.get(i).getZ();
					if(actualNode.getZ() == z)
						if (map[z][y][x] == Raster.FORBIDDEN || map[z][y][x] == Raster.OBSTACLE) {
							return false;
					} else
						continue;
				}
				return true;
			}
		}
		return true;
	}
	
	private ArrayList<Node> commonPart(ArrayList<Node> list1, ArrayList<Node> list2) {
		ArrayList<Node> listCommon = new ArrayList<Node>();
		
		for(int i = 0; i < list1.size(); i++)
			for(int j = 0; j < list2.size(); j++) {
				if (isSameNode(list1.get(i), list2.get(j)))
					listCommon.add(list1.get(i));
			}
		return listCommon;
	}

	public void perform(boolean isChebyshev) {
		ArrayList<Node> closedset = new ArrayList<Node>();
		ArrayList<Node> openset = new ArrayList<Node>();
		ArrayList<Node> neighbours;

		double g, h, f;
		g = 0.0;
		
		if (isChebyshev) {
			h = count_h_chebyshev(startNode);
		} else {
			h = count_h_manhattan(startNode);
		}
		f = g + h;
		startNode.setGHF(g, h, f);
		startNode.setParent(startNode);

		printNode(startNode);
		printNode(endNode);
		openset.add(startNode);
		// openset.addAll(this.getNeighboursManhattan(startNode));
		// int x;

		boolean isPath = false;
		int x = 0;

		while (!openset.isEmpty()) {
			x = getLowestFCostIndex(openset);
			Node actualNode = openset.get(x);

			if (isSameNode(openset.get(x), endNode)) {
				isPath = true;
				break;
			}

			if (isChebyshev) {
				neighbours = getNeighboursChebyshev(actualNode);
			} else {
				neighbours = getNeighboursManhattan(actualNode);
			}
			
			printList(neighbours);
			closedset.add(actualNode);
			openset.remove(x);

			for (int i = 0; i < neighbours.size(); i++) {
				if (isInSet(neighbours.get(i), closedset))
					continue;

				Node neighbour = neighbours.get(i);
				if (isChebyshev) {
					g = actualNode.getG() + getChebyshevCost(actualNode,neighbour);
				} else {
					g = actualNode.getG() + 1.0;
				}

				if (!isInSet(neighbour, openset)) {
					neighbour.setParent(actualNode);
					if (isChebyshev) {
						h = count_h_chebyshev(neighbour);
					} else {
						h = count_h_manhattan(neighbour);
					}
					
					f = g + h;
					neighbour.setGHF(g, h, f);

					openset.add(neighbour);
				} else {
					int y = getNodeFromList(neighbour, openset);
					neighbour = openset.get(y);
					if (g < openset.get(y).getG()) {
						neighbour.setParent(actualNode);
						f = g + neighbour.getH();
						neighbour.setG(g);
						neighbour.setF(f);

						openset.set(y, neighbour);
					}
				}
			}
			// animacja
			if (renderMap.isAnimation()) {
				renderMap.stepAstar(openset, closedset, actualNode);
			}
		}

		if (!isPath) {
			System.out.println("Nie znaleziono œcie¿ki");
		} else {
			System.out.println("Znaleziono œcie¿kê");
			reconstructPath(openset.get(x));
		}
	}

	private void reconstructPath(Node endNode) {
		path = new ArrayList<Node>();

		Node currentNode = endNode;
		path.add(currentNode);

		while (!isSameNode(currentNode, currentNode.getParent())) {
			printNode(currentNode);
			printNode(currentNode.getParent());
			currentNode = currentNode.getParent();
			path.add(currentNode);
		}

		path.remove(0);
		path.remove(path.size() - 1);
	}

	private boolean isInSet(Node currentNode, ArrayList<Node> list) {
		for (int i = 0; i < list.size(); i++) {
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

	private int getNodeFromList(Node currentNode, ArrayList<Node> list) {
		for (int i = 0; i < list.size(); i++) {
			if (isSameNode(list.get(i), currentNode))
				return i;
		}
		return -1;
	}

	private int getLowestFCostIndex(ArrayList<Node> openset) {
		double lowestFCost = openset.get(0).getF();
		int index = 0;

		for (int i = 1; i < openset.size(); i++) {
			if (openset.get(i).getF() < lowestFCost) {
				lowestFCost = openset.get(i).getF();
				index = i;
			}
		}
		return index;
	}

	private double count_h_manhattan(Node currentNode) {
		return Math.abs(currentNode.getZ() - endNode.getZ()) + Math.abs(currentNode.getY() - endNode.getY())
				+ Math.abs(currentNode.getX() - endNode.getX());
	}

	private double count_h_chebyshev(Node currentNode) {
		return Math.sqrt(Math.pow(currentNode.getZ() - endNode.getZ(), 2)
				+ Math.pow(currentNode.getY() - endNode.getY(), 2) + Math.pow(currentNode.getX() - endNode.getX(), 2));
	}

	// check if a raster is in map and if it is not obstacle or forbidden
	private boolean checkNode(int z, int y, int x) {

		if (z < sizeZ && z >= 0) {
			if (y < sizeY && y >= 0) {
				if (x < sizeX && x >= 0) {

					if (map[z][y][x] != Raster.FORBIDDEN && map[z][y][x] != Raster.OBSTACLE) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private ArrayList<Node> possibleNeighboursManhattan(Node current) {
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

	private ArrayList<Node> possibleNeighboursChebyshev(Node current) {
		int currentX = current.getX();
		int currentY = current.getY();
		int currentZ = current.getZ();

		ArrayList<Node> possibleNeighbours = new ArrayList<Node>();
		for (int z = currentZ - 1; z <= currentZ + 1; z++)
			for (int y = currentY - 1; y <= currentY + 1; y++)
				for (int x = currentX - 1; x <= currentX + 1; x++) {
					if (!(x == currentX && y == currentY && z == currentZ))
						possibleNeighbours.add(new Node(z, y, x));
				}

		return possibleNeighbours;
	}

	private ArrayList<Node> getNeighboursManhattan(Node currentNode) {

		ArrayList<Node> possibleNeighbours = possibleNeighboursManhattan(currentNode);
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (int i = 0; i < possibleNeighbours.size(); i++) {
			Node testNode = possibleNeighbours.get(i);
			if (checkNode(testNode.getZ(), testNode.getY(), testNode.getX()))
				neighbours.add(testNode);
		}

		return neighbours;
	}

	private ArrayList<Node> getNeighboursChebyshev(Node currentNode) {

		ArrayList<Node> possibleNeighbours = possibleNeighboursChebyshev(currentNode);
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (int i = 0; i < possibleNeighbours.size(); i++) {
			Node testNode = possibleNeighbours.get(i);
			if (checkNode(testNode.getZ(), testNode.getY(), testNode.getX()))
				if (checkPossibleTransition(currentNode, testNode))
					neighbours.add(testNode);
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
