package optimumPath.algorithms;

import java.util.ArrayList;

import optimumPath.common.Raster;
import optimumPath.object.Map;

public class WavePropagation extends Algorithm{

	public WavePropagation(Map inputMap) {
		super(inputMap);
	}
	
	private ArrayList<Node> getFreeRasterList(){
		ArrayList<Node> FreeRasters = new ArrayList<Node>();
		
		for (int z = 0; z < this.getSizeZ(); z++) {
			for (int y = 0; y < this.getSizeY(); y++) {
				for (int x = 0; x < this.getSizeX(); x++) {
					if (getMap()[z][y][x] == Raster.EMPTY) {
						FreeRasters.add(new Node(z, y, x));
					}
					if (getMap()[z][y][x] == Raster.START) {
						FreeRasters.add(new Node(z, y, x, Raster.START));
					}

				}
			}
		}
		return FreeRasters;
	}
	
	protected ArrayList<Node> getObstacleForbiddenList(){
		ArrayList<Node> ForbidRasters = new ArrayList<Node>();
		
		for (int z = 0; z < this.getSizeZ(); z++) {
			for (int y = 0; y < this.getSizeY(); y++) {
				for (int x = 0; x < this.getSizeX(); x++) {
					if (getMap()[z][y][x] == Raster.OBSTACLE && getMap()[z][y][x] == Raster.FORBIDDEN) {
						ForbidRasters.add(new Node(z, y, x));
					}
				}
			}
		}
		return ForbidRasters;
	}
	
	private void removeNodeFromList(Node currentNode, ArrayList<Node> list) {
		for (int i = 0; i < list.size(); i++) {
			if (isSameNode(list.get(i), currentNode)) {
				list.remove(i);
				break;
			}
		}
	}
	
	private ArrayList<Node> copyListNode(ArrayList<Node> list) {
		ArrayList<Node> copyList = new ArrayList<Node>();
		for (int i = 0; i < list.size(); i++) {
			Node tmpNode = list.get(i);
			Node newNode = new Node(tmpNode.getZ(), tmpNode.getY(), tmpNode.getX(), tmpNode.getType());
			newNode.setF(tmpNode.getF());
			copyList.add(newNode);
		}
		
		return copyList;
	}
	
	/////////////////////////////////////////////////////
	//	GLOWNY ALGORYTM PROPAGACJI FALI
	
	public void perform(boolean isChebyshev) {
		ArrayList<Node> freeRasters = getFreeRasterList();
		
		ArrayList<Node> actualNodes = new ArrayList<Node>();
		ArrayList<Node> processedNodes = new ArrayList<Node>();
		ArrayList<Node> prevNodes;
		
		getEndNode().setF(0.0);
		actualNodes.add(getEndNode());
		processedNodes.add(getEndNode());
		
		//przypisanie poczatkowych wartosci wolnym rastrom
		for(int i = 0; i < freeRasters.size(); i++) {
			freeRasters.get(i).setF(getSizeX()*getSizeY()*getSizeZ());
		}
		
		boolean isPath = false;
		int iteration = 1;
		
		while (!actualNodes.isEmpty()) {
			prevNodes = copyListNode(actualNodes);
			actualNodes.clear();
			
			for (int i = 0; i < prevNodes.size(); i++) {
				ArrayList<Node> neighbours;
				if (isChebyshev) {
					neighbours = possibleNeighboursChebyshev(prevNodes.get(i));
				} else {
					neighbours = possibleNeighboursManhattan(prevNodes.get(i));
				}
				
				for (int n = 0; n < neighbours.size(); n++) {
					if (isInSet(neighbours.get(n), freeRasters)) {
						if (checkPossibleTransition(prevNodes.get(i), neighbours.get(n))) {
							neighbours.get(n).setF(iteration);
							removeNodeFromList(neighbours.get(n), freeRasters);
							actualNodes.add(neighbours.get(n));
							
						}
					}
				}
			}
			
			
			// animacja
			if (renderMap.isAnimation()) {
				renderMap.stepAstar(prevNodes, processedNodes, new Node(0,0,0));
			}
			
			processedNodes.addAll(prevNodes);
			prevNodes.clear();
			
			int index = getNodeFromList(getStartNode(), actualNodes);
			if (index != -1) {
				setStartNode(actualNodes.get(index));
				isPath = true;
				break;
			}
			
			iteration += 1;
		}
		
		if (!isPath) {
			System.out.println("Nie znaleziono œcie¿ki");
		} else {
			System.out.println("Znaleziono œcie¿kê");
			reconstructPath(getStartNode(), processedNodes, isChebyshev);
		}
	}
	
	private void reconstructPath(Node node, ArrayList<Node> processedNodes, boolean isChebyshev) {
		setPath(new ArrayList<Node>());
		getPath().add(node);

		Node lastNodePath = getPath().get(getPath().size() - 1);
		while (lastNodePath.getF() != 0.0) {
			
			ArrayList<Node> neighbours;
			if (isChebyshev) {
				neighbours = possibleNeighboursChebyshev(lastNodePath);
				sortNeighbours(lastNodePath, neighbours);
			} else {
				neighbours = possibleNeighboursManhattan(lastNodePath);
			}
			
			for (int i = 0; i < neighbours.size(); i++) {
				int index = getNodeFromList(neighbours.get(i), processedNodes);
				if (index != -1) {
					if (processedNodes.get(index).getF() == lastNodePath.getF()-1) {
						if (checkPossibleTransition(lastNodePath, processedNodes.get(index))) {
							getPath().add(processedNodes.get(index));
							lastNodePath = getPath().get(getPath().size() - 1);
							break;
						}
					}
				}
			}
		}

		getPath().remove(0);
		getPath().remove(getPath().size() - 1);
	}
	
	////////////////////////////////////////////////////////

	private void sortNeighbours(Node currentNode, ArrayList<Node> neighbours) {
		ArrayList<Node> corners = new ArrayList<Node>();
		ArrayList<Node> edges = new ArrayList<Node>();
		ArrayList<Node> centers = new ArrayList<Node>();
		
		for(int i = 0; i < neighbours.size(); i++) {
			switch(checkChebyshevDirection(currentNode, neighbours.get(i))) {
				case 3:
					corners.add(neighbours.get(i));
					break;
				case 2:
					edges.add(neighbours.get(i));
					break;
				case 1:
					centers.add(neighbours.get(i));
				default:
					break;
			}
		}
		neighbours.clear();
		neighbours.addAll(centers);
		neighbours.addAll(edges);
		neighbours.addAll(corners);
	}
}
