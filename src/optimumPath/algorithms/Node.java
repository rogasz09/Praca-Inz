package optimumPath.algorithms;

import optimumPath.common.*;

public class Node {
	
	private int z;
	private int y;
	private int x;
	
	private double g;
	private double h;
	private double f;
	
	private Node parent;
	Raster type;
	
	Node(int z, int y, int x){
		this(z, y, x, Raster.EMPTY);
		
	}
	
	Node(int z, int y, int x, Raster type){
		setZ(z);
		setY(y);
		setX(x);
		setType(type);
		
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
		
	}
	public int getParentZ() {
		return parent.z;
		
	}
	public int getParentY() {
		return parent.y;
		
	}
	public int getParentX() {
		return parent.x;
		
	}
	public Raster getParentType() {
		return parent.type;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public Raster getType() {
		return type;
	}

	public void setType(Raster type) {
		this.type = type;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}
	
	public void setGHF(double g, double h, double f) {
		this.g = g;
		this.h = h;
		this.f = f;
	}
	
}
