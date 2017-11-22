package optimumPath.algorithms;

import optimumPath.common.*;

public class Node {
	
	private int z;
	private int y;
	private int x;
	
	
	
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
	
}
