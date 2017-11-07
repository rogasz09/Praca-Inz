package optimumPath.frame;

import javax.swing.JButton;
import java.awt.Dimension;


public class ToolBarButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6468386861235189417L;

	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(30, 30);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(30, 30);
	}
	
	
}
