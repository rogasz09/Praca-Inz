package windowBuilder.views;

import javax.swing.JButton;
import java.awt.Dimension;


public class ToolBarButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(25, 25);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(25, 25);
	}
}
