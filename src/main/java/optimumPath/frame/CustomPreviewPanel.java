package optimumPath.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

public class CustomPreviewPanel extends JComponent {
	 
	private static final long serialVersionUID = 1304815424234178714L;
	private Color curColor;
	
	public CustomPreviewPanel(JColorChooser chooser) {
		curColor = chooser.getColor();
		setPreferredSize(new Dimension(100, 50));
	}
	
	public void paint(Graphics g) {
		g.setColor(curColor);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	public Color getCurColor() {
		return curColor;
	}

	public void setCurColor(Color curColor) {
		this.curColor = curColor;
	}

}
