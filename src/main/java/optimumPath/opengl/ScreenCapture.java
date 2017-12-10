package optimumPath.opengl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.GLBuffers;

public class ScreenCapture {
	private ByteBuffer buffer;
	private int CanvasWidth, CanvasHeight;
	
	private volatile boolean takingScreenshot = false;
	
    //////////////////////////////
    // konstruktory
	public ScreenCapture() {
		this.CanvasWidth = 1;
		this.CanvasHeight = 1;
	}
	
	public ScreenCapture(int CanvasWidth, int CanvasHeight) {
		this.CanvasWidth = CanvasWidth;
		this.CanvasHeight = CanvasHeight;
	}
	
	public ScreenCapture(GLCanvas canvas) {
		getSizeFromCanvas(canvas);
	}
	//////////////////////////////
	
	//komunikaty bledu
	public static void errorBox(String errorMessage, String errorTitle) {
        JOptionPane.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
    }
	
	//zapisanie aktualnego widoku do bufora
	public void takeScreenshot(GL2 gl) {
		if (buffer != null)
			buffer.clear();
		
		buffer = GLBuffers.newDirectByteBuffer(CanvasWidth * CanvasHeight * 4);

		gl.glReadBuffer(GL2.GL_BACK);
		gl.glReadPixels(0, 0, CanvasWidth, CanvasHeight, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);
		takingScreenshot = false;
	}
	
	// tworzenie screenshot funkcji
	private BufferedImage makeScreenshot() {
		BufferedImage screenshot = new BufferedImage(CanvasWidth, CanvasHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = screenshot.getGraphics();

		try {
			for (int h = 0; h < CanvasHeight; h++) {
				for (int w = 0; w < CanvasWidth; w++) {
					graphics.setColor(new Color((buffer.get() & 0xff), (buffer.get() & 0xff), (buffer.get() & 0xff)));
					buffer.get();
					graphics.drawRect(w, CanvasHeight - h, 1, 1);
				}
			}
			
			buffer.clear();
		} catch (Exception ex) {
			ex.printStackTrace();
			errorBox("B³¹d w trakcie wczytywania do bufora", "B³¹d bufora");
			takingScreenshot = false;
		}
		
		return screenshot;
	}

	//zapisanie obrazu do pliku
	public void saveImage(String pathFile) {
		try {
			BufferedImage screenshot = makeScreenshot();

			ImageIO.write(screenshot, "PNG", new File(pathFile));
		} catch (IOException ex) {
			ex.printStackTrace();
			errorBox("Wyst¹pi³ b³¹d w trakcie zapisu widoku okna Canvas", "B³¹d zapisu");
			takingScreenshot = false;
		}
	}
	
	public void takeScreen() throws InvocationTargetException, InterruptedException {
		takingScreenshot = true;
		
		//czekaj na wypelnienie bufora
		while(takingScreenshot) {}
		
		if(!takingScreenshot)
			JOptionPane.showMessageDialog(null, "Zrobiono zdjêcie widoku.\nWybierz miejsce zapisu pliku.", "Screenshot", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//zapisanie z oknem zapisu
	public void saveScreenToPNG() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG (*.png)", "PNG", "png");
		fc.setFileFilter(filter);
		
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	String filePath = fc.getSelectedFile().getPath();
	    	
	    	if (fc.getFileFilter().getDescription() == "PNG (*.png)" && !filePath.endsWith(".png"))
	    		filePath += ".png";
	    	
	    	System.out.println("Saving: " + filePath);
	    	saveImage(filePath);
		}
	}
	
	
	//////////////////////////////////
	// getty
	public void getSizeFromCanvas(GL2 gl) {
		int viewport[] = new int[4];
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
		
		CanvasWidth = viewport[2];
		CanvasHeight = viewport[3];
	}
	
	public void getSizeFromCanvas(GLCanvas canvas) {
		CanvasWidth = canvas.getWidth();
		CanvasHeight = canvas.getHeight();
	}

	public boolean isTakingScreenshot() {
		return takingScreenshot;
	}
	
	//////////////////////////////////
}
