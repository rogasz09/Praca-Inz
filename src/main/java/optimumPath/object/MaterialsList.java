package optimumPath.object;

import java.awt.Color;

import com.jogamp.opengl.GL2;

public class MaterialsList {
	private final Material matObstacle;
	private final Material matObstacleMod;
	private final Material matForbidden;
	private final Material matPath;
	
	private final Material matStart;
	private final Material matEnd;
	
	private final Material matOpen;
	private final Material matClosed;
	private final Material matActual;
	
	private final Material matBase;
	private final Material matGrid;
	
	private Color background;
	
	public MaterialsList() {
		matObstacle = new Material();
		matObstacleMod = new Material();
		matForbidden = new Material();
		matPath = new Material();
		
		matStart = new Material();
		matEnd = new Material();
		
		matOpen = new Material();
		matClosed = new Material();
		matActual = new Material();
		
		matBase = new Material();
		matGrid = new Material();
		
		setDefaultMaterials();
	}
	
	public void setDefaultMaterials() {
		//base
		matBase.setAmbient(0.5f, 0.5f, 0.8f, 1.0f);
		matBase.setDiffuse(0.5f, 0.5f, 0.8f, 1.0f);
		//matBase.setSpecular(0.6f, 0.5f, 0.8f, 1.0f);
		
		//grid
		matGrid.setAmbient(1.0f, 1.0f, 1.0f, 1.0f);
		matGrid.setDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		matGrid.setSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		
		//obstacle
		matObstacle.setAmbient(0.0f, 0.0f, 0.0f, 1.0f);
		matObstacle.setDiffuse(0.1f, 0.5f, 0.8f, 0.85f);
		matObstacle.setSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		matObstacle.setShininess(5.0f);
		
		//obstacle modification
		matObstacleMod.setDiffuse(0.5f, 0.1f, 0.8f, 0.5f);
		
		//forbidden
		matForbidden.setAmbient(0.0f, 0.0f, 0.0f, 1.0f);
		matForbidden.setDiffuse(0.1f, 0.5f, 0.8f, 0.3f);
		matForbidden.setSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		matForbidden.setShininess(5.0f);
		
		//path
		//matPath.setAmbient(0.8f, 0.8f, 0.2f, 1.0f);
		matPath.setDiffuse(0.6f, 0.0f, 0.0f, 0.9f);
		matPath.setSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		matPath.setShininess(5.0f);
		
		//start
		matStart.setDiffuse(1.0f, 0.8f, 0.1f, 1.0f);
		
		//end
		matEnd.setDiffuse(1.0f, 0.2f, 0.1f, 1.0f);
		
		//open
		matOpen.setDiffuse(0.9f, 0.9f, 0.9f, 0.4f);
		
		//closed
		matClosed.setDiffuse(0.1f, 0.1f, 0.1f, 0.5f);
		
		//actual
		matActual.setDiffuse(0.1f, 0.7f, 0.1f, 0.6f);
		
		//background color
		background = Color.WHITE;
	}
	
	/////////////////////////////////////////
	// Getters and Setters
	
	public void setGLBackground(GL2 gl) {
		float[] f_background = Material.ColorToFloat(background);
		gl.glClearColor(f_background[0], f_background[1], f_background[2], f_background[3]);
	}

	public Material getMatObstacle() {
		return matObstacle;
	}

	public Material getMatObstacleMod() {
		return matObstacleMod;
	}

	public Material getMatForbidden() {
		return matForbidden;
	}

	public Material getMatPath() {
		return matPath;
	}

	public Material getMatStart() {
		return matStart;
	}

	public Material getMatEnd() {
		return matEnd;
	}

	public Material getMatOpen() {
		return matOpen;
	}

	public Material getMatClosed() {
		return matClosed;
	}

	public Material getMatActual() {
		return matActual;
	}

	public Material getMatBase() {
		return matBase;
	}

	public Material getMatGrid() {
		return matGrid;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}
	
	////////////////////////////////////////
}
