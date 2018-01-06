package optimumPath.algorithms;

import java.util.ArrayList;

import optimumPath.common.*;
import optimumPath.object.Map;

public class MazeController extends Algorithm {
	private GeneticAlgorithm geneticAlgorithm;
	public ArrayList<Integer> fittestDirections;
	
	private boolean optimization = false;

	public MazeController(Map inputMap) {
		super(inputMap);
	}


	public Point3d Move(Point3d position, int direction) {
		Node testNode;

		switch (direction) {
		case 0: // North
			testNode = new Node((int) position.getZ(), (int) (position.getY() - 1), (int) position.getX());
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setY(position.getY() - 1);
			}
			break;
		case 1: // South
			testNode = new Node((int) position.getZ(), (int) (position.getY() + 1), (int) position.getX());
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setY(position.getY() + 1);
			}
			break;
		case 2: // East
			testNode = new Node((int) position.getZ(), (int) position.getY(), (int) (position.getX() + 1));
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setX(position.getX() + 1);
			}
			break;
		case 3: // West
			testNode = new Node((int) position.getZ(), (int) position.getY(), (int) (position.getX() - 1));
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setX(position.getX() - 1);
			}
			break;
		case 4: // North East
			testNode = new Node((int) position.getZ(), (int) (position.getY() - 1), (int) (position.getX() + 1));
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setY(position.getY() - 1);
				position.setX(position.getX() + 1);
			}
			break;
		case 5: // North West
			testNode = new Node((int) position.getZ(), (int) (position.getY() - 1), (int) (position.getX() - 1));
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setY(position.getY() - 1);
				position.setX(position.getX() - 1);
			}
			break;
		case 6: // South East
			testNode = new Node((int) position.getZ(), (int) (position.getY() + 1), (int) (position.getX() + 1));
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setY(position.getY() + 1);
				position.setX(position.getX() + 1);
			}
			break;
		case 7: // South West
			testNode = new Node((int) position.getZ(), (int) (position.getY() + 1), (int) (position.getX() - 1));
			if (!checkNode(testNode)) {
				break;
			} else {
				position.setY(position.getY() + 1);
				position.setX(position.getX() - 1);
			}
			break;
		}
		
		return position;
	}

	public double TestRoute(ArrayList<Integer> directions) {
		Point3d position = this.getStartNode().getPosition();

		for (int directionIndex = 0; directionIndex < directions.size(); directionIndex++) {
			int nextDirection = directions.get(directionIndex);
			position = Move(position, nextDirection);
		}

		Point3d deltaPosition = new Point3d(Math.abs(position.getX() - this.getEndNode().getX()),
				Math.abs(position.getY() - this.getEndNode().getY()),
				Math.abs(position.getZ() - this.getEndNode().getZ()));
		double result = 1 / (double) (deltaPosition.getX() + deltaPosition.getY() + 1);
		if (result == 1)
			System.out.println("TestRoute result=" + result + ",(" + position.getX() + "," + position.getY() + ")");
		return result;
	}

	@Override
	public void perform(boolean isChebyshev) {
		fittestDirections = new ArrayList<Integer>();
		this.setPath(new ArrayList<Node>());

		if(isChebyshev)
			geneticAlgorithm = new GeneticAlgorithm(this, 3);
		else
			geneticAlgorithm = new GeneticAlgorithm(this, 2);
		
		geneticAlgorithm.mazeController = this;
		geneticAlgorithm.Run();

		while (geneticAlgorithm.busy) {
			geneticAlgorithm.Epoch();
			RenderFittestChromosomePath();
		}
		
		if(optimization)
			optimizationPath2();
		
		getPath().add(0, this.getStartNode());
		getPath().add(this.getEndNode());
		
		
		
		printList(getPath());
		System.out.println("D³ugoœæ œcie¿ki: " + getPath().size());

		System.out.println("Iloœæ generacji: " + this.geneticAlgorithm.getGeneration());
	}
	
	public void optimizationPath() {
		ArrayList<Node> optPath = new ArrayList<Node>();
		
		for (int i = 0; i < getPath().size(); i++) {
			if(!isInSet(getPath().get(i), optPath))
				optPath.add(getPath().get(i));
		}
		
		setPath(optPath);
	}
	
	public void optimizationPath2() {
		for (int i = 0; i < getPath().size(); i++) {
			Node temp = getPath().get(i);
			for (int j = getPath().size() - 1; j > i; j--) {
				if (j >= getPath().size())
					break;
				
				if (isSameNode(temp, getPath().get(j))) {
					//int count = j - i;
					for(int k = j; k > i; k--) {
						getPath().remove(k);
					}
				}
			}
		}
	}

	public void RenderFittestChromosomePath() {
		this.getPath().clear();
		Genome fittestGenome = geneticAlgorithm.genomes.get(geneticAlgorithm.fittestGenome);
		ArrayList<Integer> fittestDirections = geneticAlgorithm.Decode(fittestGenome.getBits());
		Point3d position = this.getStartNode().getPosition();

		for (int direction : fittestDirections) {
			position = Move(position, direction);
			Node pathNode = new Node((int) position.getZ(), (int) position.getY(), (int) position.getX());

			if (!isSameNode(pathNode, this.getStartNode()) && !isSameNode(pathNode, this.getEndNode()))
				this.getPath().add(pathNode);
		}
	}

	public boolean isOptimization() {
		return optimization;
	}

	public void setOptimization(boolean optimization) {
		this.optimization = optimization;
	}
}
