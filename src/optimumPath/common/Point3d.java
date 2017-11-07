package optimumPath.common;

public class Point3d {

	private double x, y, z;
	
	//////////////////////
	// konstruktory
	
	public Point3d() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}

	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3d(Point3d point) {
		x = point.getX();
		y = point.getY();
		z = point.getZ();
	}
	
	
	////////////////////////
	
	public void addPoint(Point3d point) {
		x += point.getX();
		y += point.getY();
		z += point.getZ();
	}
	
	public void subPoint(Point3d point) {
		x -= point.getX();
		y -= point.getY();
		z -= point.getZ();
	}
	
	public boolean equalsPoint(double x, double y, double z) {
		if (this.x == x && this.y == y && this.z == z)
			return true;
		
		return false;
	}
	
	
	////////////////////////
	// gettters and setters
	
	public void setPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setPoint(Point3d point) {
		x = point.getX();
		y = point.getY();
		z = point.getZ();
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	////////////////////////////
}
