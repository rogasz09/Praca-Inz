package optimumPath.main;

import java.awt.EventQueue;
import javax.swing.UIManager;

import optimumPath.window.*;
import optimumPath.opengl.*;

public class RunOptimumPath {

	/**
	 * Uruchomienie aplikacji.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					Render render = new Render();
					WindowMain window = new WindowMain(render);
					window.setVisible(true);
					
					render.getGlcanvas().setSize(window.getGLpanel().getSize());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
