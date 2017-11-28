package optimumPath.algorithms;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import optimumPath.common.Raster;
import optimumPath.object.Map;

public class Algorithm {
	private Raster map[][][];
	private int sizeX;
	private int sizeY;
	private int sizeZ;

	private Node startNode;
	private Node endNode;

	private ArrayList<Node> path;
	protected Map renderMap;
	
	protected int numberIteration;
	protected int numberRasterPath;
	protected double lengthPath;

	public Algorithm(Map inputMap) {
		initMap(inputMap);
	}

	public void initMap(Map inputMap) {
		this.renderMap = inputMap;
		this.sizeZ = renderMap.getSizeZ();
		this.sizeY = renderMap.getSizeY();
		this.sizeX = renderMap.getSizeX();
		this.map = new Raster[sizeZ][sizeY][sizeX];

		// I madethis initialization, in case to avoid problems with no start or end
		// point
		startNode = null;
		endNode = null;
		
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
	
	/*****************************************************
		Metody do wypisywania w konsoli
		Uzywane do debugowania
	*****************************************************/
	
	// wypisuje w kosoli mape raster
	public void printMap(Raster[][][] inputMap) {
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

	// wypisuje w konsoli liste Nodow
	public void printList(ArrayList<Node> list) {
		System.out.print("List: ");
		for (int i = 0; i < list.size(); i++) {
			printNode(list.get(i));
		}
		System.out.print('\n');
	}

	// wypisuje w konsoli Node
	public void printNode(Node node) {
		System.out.print("( ");
		System.out.print(Integer.toString(node.getX()) + " ");
		System.out.print(Integer.toString(node.getY()) + " ");
		System.out.print(Integer.toString(node.getZ()) + " ");
		System.out.print("), ");
	}
	/*****************************************************/
	
	
	public static void issueInfoBox(String infoMessage, String issueTitle) {
        JOptionPane.showMessageDialog(null, infoMessage, "Problem: " + issueTitle, JOptionPane.INFORMATION_MESSAGE);
    }
	
	public boolean checkSatartEnd() {
		if (!renderMap.isStart() && !renderMap.isEnd()) {
			issueInfoBox("Na mapie nie zaznaczono punktu startowego i ko�cowego", "brak punktu start i koniec");
			return false;
		}
		if (!renderMap.isStart()) {
			issueInfoBox("Na mapie nie zaznaczono punktu startowego", "brak punktu start");
			return false;
		}
		if (!renderMap.isEnd()) {
			issueInfoBox("Na mapie nie zaznaczono punktu ko�cowego", "brak punktu koniec");
			return false;
		}
		return true;
	}
	
	public void noPath() {
		issueInfoBox("Algorytm nie znalaz� scie�ki", "brak �cie�ki");
	}
	
	// zapisuje sciezke do mapy rastrow
	public void writePathToMap(Raster[][][] rasterMap) {
		if (path == null)
			return;

		for (int i = 0; i < path.size(); i++) {
			Node currentNode = path.get(i);
			rasterMap[currentNode.getZ()][currentNode.getY()][currentNode.getX()] = Raster.PATH;
		}
	}
	/*****************************************************/
	
	
	
	/*****************************************************
	 METODY OBSLUGI ALGORYTMU
	 *****************************************************/
	
	// zwraca wartosc true lub false w zaleznosci czy znajduje sie
	// w liscie nodow podany node
	protected boolean isInSet(Node currentNode, ArrayList<Node> list) {
		for (int i = 0; i < list.size(); i++) {
			if (isSameNode(list.get(i), currentNode))
				return true;
		}
		return false;
	}
	
	// zwraca indeks w liscie nodow dla znlezionego noda
	// jesli node nie zostal znaleziony, metoda zwraca -1
	protected int getNodeFromList(Node currentNode, ArrayList<Node> list) {
		for (int i = 0; i < list.size(); i++) {
			if (isSameNode(list.get(i), currentNode))
				return i;
		}
		return -1;
	}

	// porownuje dwa nody
	// jesli sa takie same to zwraca true a jesli nie to false
	protected boolean isSameNode(Node node1, Node node2) {
		if (node1.getZ() == node2.getZ())
			if (node1.getY() == node2.getY())
				if (node1.getX() == node2.getX())
					return true;

		return false;
	}
	/*****************************************************/


	
	/*****************************************************
	 Metody dla metryki Chebyshev
	 *****************************************************/
	
	protected int checkChebyshevDirection(Node actualNode, Node neighbour) {
		int sameX,sameY,sameZ;
		int sum = 0;
		sameX = (actualNode.getX()==neighbour.getX()) ? 0 : 1;
		sameY = (actualNode.getY()==neighbour.getY()) ? 0 : 1;
		sameZ = (actualNode.getZ()==neighbour.getZ()) ? 0 : 1;
		sum = sameX + sameY + sameZ;
		return sum;
	}

	protected boolean checkPossibleTransition(Node actualNode,Node neighbour) {
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
	
	protected ArrayList<Node> commonPart(ArrayList<Node> list1, ArrayList<Node> list2) {
		ArrayList<Node> listCommon = new ArrayList<Node>();
		
		for(int i = 0; i < list1.size(); i++)
			for(int j = 0; j < list2.size(); j++) {
				if (isSameNode(list1.get(i), list2.get(j)))
					listCommon.add(list1.get(i));
			}
		return listCommon;
	}
	
	protected ArrayList<Node> possibleNeighboursChebyshev(Node current) {
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
	/*****************************************************/
	
	
	
	
	/*****************************************************
	 Metody dla metryki Manhattan
	 *****************************************************/
	
	protected ArrayList<Node> possibleNeighboursManhattan(Node current) {
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
	/*****************************************************/
	
	//////////////////////////////////////////////////////
	// Getters and Setters
	public Raster[][][] getMap() {
		return map;
	}

	public void setMap(Raster[][][] map) {
		this.map = map;
	}

	public Map getRenderMap() {
		return renderMap;
	}

	public void setRenderMap(Map renderMap) {
		this.renderMap = renderMap;
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

	public ArrayList<Node> getPath() {
		return path;
	}

	public void setPath(ArrayList<Node> path) {
		this.path = path;
	}

	public int getNumberIteration() {
		return numberIteration;
	}

	public void setNumberIteration(int numberIteration) {
		this.numberIteration = numberIteration;
	}

	public int getNumberRasterPath() {
		return numberRasterPath;
	}

	public void setNumberRasterPath(int numberRasterPath) {
		this.numberRasterPath = numberRasterPath;
	}

	public double getLengthPath() {
		return lengthPath;
	}

	public void setLengthPath(double lengthPath) {
		this.lengthPath = lengthPath;
	}
	
	//////////////////////////////////////////////
}
