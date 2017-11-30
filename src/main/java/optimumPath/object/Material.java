package optimumPath.object;

import java.awt.Color;

import com.jogamp.opengl.GL2;

public class Material {
	private float ambient[];
	private float diffuse[]; 
	private float specular[];
	private float shininess[];
	
	public Material() {
		ambient = new float[4];
		diffuse = new float[4];
		specular = new float[4];
		shininess = new float[1];
		
		initMaterial();
	}
	
	private void initMaterial() {
		setAmbient(0.0f, 0.0f, 0.0f, 0.0f);
		setDiffuse(0.0f, 0.0f, 0.0f, 0.0f);
		setSpecular(0.0f, 0.0f, 0.0f, 0.0f);
		setShininess(0.0f);
	}
	
	/****************************************
	 set material opengl
	 ****************************************/
	
	public void setGLMaterial(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);
	}
	
	public void setGLAmbient(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	}
	
	public void setGLDiffuse(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	}
	
	public void setGLSpecular(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
	}
	
	public void setGLShininess(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);
	}
	/****************************************/
	
	
	
	/****************************************
	 gettres and settres
	 ****************************************/
	
	////////////////////////////////////////////
	// ambient
	public float[] getAmbient() {
		return ambient;
	}
	
	public Color getAmbientColor() {
		return new Color(ambient[0], ambient[1], ambient[2], ambient[3]);
	}

	public void setAmbient(float[] ambient) {
		this.ambient = ambient;
	}
	
	public void setAmbient(float R, float G, float B, float A) {
		setMaterial(this.ambient, R, G, B, A);
	}
	
	public void setAmbient(Color color) {
		float[] ambient = ColorToFloat(color);
		setAmbient(ambient);
	}
	/////////////////////////////////////////////

	
	/////////////////////////////////////////////
	// diffuse
	public float[] getDiffuse() {
		return diffuse;
	}
	
	public Color getDiffuseColor() {
		return new Color(diffuse[0], diffuse[1], diffuse[2], diffuse[3]);
	}

	public void setDiffuse(float[] diffuse) {
		this.diffuse = diffuse;
	}
	
	public void setDiffuse(float R, float G, float B, float A) {
		setMaterial(this.diffuse, R, G, B, A);
	}
	
	public void setDiffuse(Color color) {
		float[] diffuse = ColorToFloat(color);
		setDiffuse(diffuse);
	}
	/////////////////////////////////////////////
	

	/////////////////////////////////////////////
	// specular
	public float[] getSpecular() {
		return specular;
	}
	
	public Color getSpecularColor() {
		return new Color(specular[0], specular[1], specular[2], specular[3]);
	}

	public void setSpecular(float[] specular) {
		this.specular = specular;
	}
	
	public void setSpecular(float R, float G, float B, float A) {
		setMaterial(this.specular, R, G, B, A);
	}
	
	public void setSpecular(Color color) {
		float[] specular = ColorToFloat(color);
		setSpecular(specular);
	}
	/////////////////////////////////////////////

	
	/////////////////////////////////////////////
	// shininess
	public float[] getShininess() {
		return shininess;
	}
	
	public float getShininessValue() {
		return shininess[0];
	}
	
	public void setShininess(float value) {
		this.shininess[0] = value;
	}
	///////////////////////////////////////
	
	/********************************************/
	
	public static float[] ColorToFloat(Color color) {
		float[] colorFloat = new float[4];
		colorFloat[0] = (float)color.getRed()/255.0f;
		colorFloat[1] = (float)color.getGreen()/255.0f;
		colorFloat[2] = (float)color.getBlue()/255.0f;
		colorFloat[3] = (float)color.getAlpha()/255.0f;

		return colorFloat;
	}
	
	private static void setMaterial(float[] material, float R, float G, float B, float A) {
		material[0] = R;
		material[1] = G;
		material[2] = B;
		material[3] = A;
	}
}
