package optimumPath.algorithms;

import java.util.ArrayList;
import java.util.Collections;

import optimumPath.common.*;
import optimumPath.object.*;

public class AStar extends Algorithm {

	public AStar(Map inputMap) {
		super(inputMap);
	}

	public void perform(boolean isChebyshev) {
		if(!checkBeforePerform())
			return;
		
		ArrayList<Node> closedset = new ArrayList<Node>();
		ArrayList<Node> openset = new ArrayList<Node>();
		ArrayList<Node> neighbours;

		double g, h, f;
		g = 0.0;
		
		if (isChebyshev) {
			h = count_h_chebyshev(getStartNode());
		} else {
			h = count_h_manhattan(getStartNode());
		}
		f = g + h;
		getStartNode().setGHF(g, h, f);
		getStartNode().setParent(getStartNode());

		openset.add(getStartNode());

		boolean isPath = false;
		int x = 0;

		numberIteration = 0;
		
		while (!openset.isEmpty() && !stopAlgorithm) {
			numberIteration += 1;
			System.out.println(numberIteration);
			
			x = getLowestFCostIndex(openset);
			Node actualNode = openset.get(x);

			if (isSameNode(openset.get(x), getEndNode())) {
				isPath = true;
				break;
			}

			if (isChebyshev) {
				neighbours = getNeighboursChebyshev(actualNode);
			} else {
				neighbours = getNeighboursManhattan(actualNode);
			}
			
			//printList(neighbours);
			closedset.add(actualNode);
			openset.remove(x);

			for (int i = 0; i < neighbours.size(); i++) {
				if (isInSet(neighbours.get(i), closedset))
					continue;
				//if (!checkNodeForbiddenRobot(neighbours.get(i), getThick()))
					//continue;
				
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
		
		if (stopAlgorithm) {
			stopAlgorithm = false;
			infoStopAlg();
			return;
		}
		writeCopyToMain();

		if (!isPath) {
			noPath();
		} else {
			System.out.println("Znaleziono œcie¿kê");
			reconstructPath(openset.get(x));
		}
		
		
	}

	private void reconstructPath(Node endNode) {
		this.setPath(new ArrayList<Node>());

		Node currentNode = endNode;
		this.getPath().add(currentNode);
		
		
		while (!isSameNode(currentNode, currentNode.getParent())) {
			//printNode(currentNode);
			//printNode(currentNode.getParent());
			currentNode = currentNode.getParent();
			getPath().add(currentNode);
		}
		
		lengthPath = endNode.getG();
		numberRasterPath = getPath().size();
		Collections.reverse(getPath());
		
		printList(getPath());
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
		//System.out.println(lowestFCost);
		return index;
	}

	private double count_h_manhattan(Node currentNode) {
		return Math.abs(currentNode.getZ() - getEndNode().getZ()) +
			   Math.abs(currentNode.getY() - getEndNode().getY()) +
			   Math.abs(currentNode.getX() - getEndNode().getX());
	}

	private double count_h_chebyshev(Node currentNode) {
		return Math.sqrt(Math.pow(currentNode.getZ() - getEndNode().getZ(), 2) + 
			   Math.pow(currentNode.getY() - getEndNode().getY(), 2) + 
			   Math.pow(currentNode.getX() - getEndNode().getX(), 2));
	}

	// check if a raster is in map and if it is not obstacle or forbidden
	private boolean checkNode(int z, int y, int x) {

		if (z < this.getSizeZ() && z >= 0) {
			if (y < this.getSizeY() && y >= 0) {
				if (x < this.getSizeX() && x >= 0) {

					if (getMap()[z][y][x] != Raster.FORBIDDEN && getMap()[z][y][x] != Raster.OBSTACLE) {
						return true;
					}
				}
			}
		}
		return false;
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

}
