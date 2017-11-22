package optimumPath.frame;

import javax.swing.JToggleButton;
import java.awt.Dimension;


public class ToolBarToggleButton extends JToggleButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5571416812761639310L;

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(30,30);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(30,30);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(30,30);
	}
	
	
}
