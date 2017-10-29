package windowBuilder.views;

import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import openGL.Render;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class Window {

	private JFrame frame;
	private JMenuBar menuBar;
	private JComboBox cbAlgorithm;
	private JComboBox cbMetrics;
	private JSlider sliderAnimSpeed;
	private JButton btnApply;
	private ToolBarButton btnNewMap;
	private ToolBarButton btnSaveMap;
	private ToolBarButton btnLoadMap;
	private ToolBarButton btnExit;
	private JPanel GLpanel;

	final private GLCanvas glcanvas;
	/**
	 * Uruchomienie aplikacji.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
					
					window.glcanvas.setSize(window.GLpanel.getSize());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * G³ówna aplikacja.
	 */
	
	public Window() {
		initComponents();
		createEvents();
		
		//getting the capabilities object of GL2 profile
	    final GLProfile profile = GLProfile.get(GLProfile.GL2);
	    GLCapabilities capabilities = new GLCapabilities(profile);
	        
	    // The canvas
	    glcanvas = new GLCanvas(capabilities);
	    Render b = new Render();
	    glcanvas.addGLEventListener(b);        
	    
	    GLpanel.add(glcanvas, BorderLayout.CENTER);
	    
		final FPSAnimator animator = new FPSAnimator(glcanvas, 300,true);
		
	    animator.start();
	}

	/////////////////////////////////////////////////////////////////////
	// Metoda zawieraj¹ca kod tworz¹cy komponenty oraz ich inicjalizacje
	/////////////////////////////////////////////////////////////////////
	
	private void initComponents() {
		frame = new JFrame();
		frame.setTitle("Optymalna \u015Bcie\u017Cka");
		frame.setBounds(100, 100, 922, 669);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		///////////////////////////////////////////////////
		// Menu
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Pliki");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMap = new JMenuItem("Nowa mapa");
		mnNewMenu.add(mntmNewMap);
		
		JMenuItem mntmSaveMap = new JMenuItem("Zapisz mape");
		mnNewMenu.add(mntmSaveMap);
		
		JMenuItem mntmLoadMap = new JMenuItem("Wczytaj mape");
		mnNewMenu.add(mntmLoadMap);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Zamknij");
		mnNewMenu.add(mntmExit);
		
		JMenu mnEdycja = new JMenu("Edycja");
		menuBar.add(mnEdycja);
		
		JMenuItem mntmDrawObstacle = new JMenuItem("Rysuj przeszkod\u0119");
		mnEdycja.add(mntmDrawObstacle);
		
		JMenuItem mntmCheckStartPoint = new JMenuItem("Zaznacz punkt startowy");
		mnEdycja.add(mntmCheckStartPoint);
		
		JMenuItem mntmCheckStopPoint = new JMenuItem("Zaznacz punkt ko\u0144cowy");
		mnEdycja.add(mntmCheckStopPoint);
		
		JPanel contentPanel = new JPanel();
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelOptions = new JPanel();
		panelOptions.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPanel.add(panelOptions, BorderLayout.EAST);
		panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.Y_AXIS));
		
		///////////////////////////////////////////////////
		// Panel mapy
		
		JPanel panelMap = new JPanel();
		panelMap.setBorder(new TitledBorder(null, "Mapa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelOptions.add(panelMap);
		
		JRadioButton rdbtnPreview = new JRadioButton("Podgl\u0105d");
		rdbtnPreview.setSelected(true);
		JRadioButton rdbtnMapMod = new JRadioButton("Modyfikacja mapy");
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnMapMod);
		group.add(rdbtnPreview);
		
		
		GroupLayout gl_panelMap = new GroupLayout(panelMap);
		gl_panelMap.setHorizontalGroup(
			gl_panelMap.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMap.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panelMap.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnMapMod)
						.addComponent(rdbtnPreview))
					.addContainerGap(93, Short.MAX_VALUE))
		);
		gl_panelMap.setVerticalGroup(
			gl_panelMap.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelMap.createSequentialGroup()
					.addContainerGap(12, Short.MAX_VALUE)
					.addComponent(rdbtnPreview)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnMapMod)
					.addContainerGap())
		);
		panelMap.setLayout(gl_panelMap);
		
		///////////////////////////////////////////////////
		// Panel algorytmów
		
		JPanel panelAlg = new JPanel();
		panelAlg.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Algorytmy", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelAlg.setToolTipText("");
		panelOptions.add(panelAlg);
		
		cbAlgorithm = new JComboBox();
		cbAlgorithm.setModel(new DefaultComboBoxModel(new String[] {"Propagacja Fali", "A Star"}));
		
		JLabel lblWybrAlgorytmu = new JLabel("Wyb\u00F3r algorytmu:");
		
		JLabel lblWybrMetryki = new JLabel("Wyb\u00F3r metryki:");
		
		cbMetrics = new JComboBox();
		cbMetrics.setModel(new DefaultComboBoxModel(new String[] {"Manhatan", "Czebyszew"}));
		GroupLayout gl_panelAlg = new GroupLayout(panelAlg);
		gl_panelAlg.setHorizontalGroup(
			gl_panelAlg.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAlg.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelAlg.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAlg.createSequentialGroup()
							.addComponent(lblWybrAlgorytmu)
							.addGap(78))
						.addGroup(gl_panelAlg.createSequentialGroup()
							.addComponent(lblWybrMetryki)
							.addContainerGap(119, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panelAlg.createSequentialGroup()
							.addGroup(gl_panelAlg.createParallelGroup(Alignment.TRAILING)
								.addComponent(cbMetrics, Alignment.LEADING, 0, 183, Short.MAX_VALUE)
								.addComponent(cbAlgorithm, 0, 183, Short.MAX_VALUE))
							.addContainerGap())))
		);
		gl_panelAlg.setVerticalGroup(
			gl_panelAlg.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAlg.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWybrAlgorytmu)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbAlgorithm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblWybrMetryki)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbMetrics, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(323, Short.MAX_VALUE))
		);
		panelAlg.setLayout(gl_panelAlg);
		
		///////////////////////////////////////////////////
		// Panel animacji
		
		JPanel panelAnim = new JPanel();
		panelAnim.setBorder(new TitledBorder(null, "Animacja", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelOptions.add(panelAnim);
		
		btnApply = new JButton("Zastosuj");
		
		sliderAnimSpeed = new JSlider();
		sliderAnimSpeed.setToolTipText("");
		sliderAnimSpeed.setSnapToTicks(true);
		sliderAnimSpeed.setPaintTicks(true);
		sliderAnimSpeed.setPaintLabels(true);
		sliderAnimSpeed.setMinorTickSpacing(1);
		sliderAnimSpeed.setValue(5);
		sliderAnimSpeed.setMaximum(10);
		
		JLabel lblSzybko = new JLabel("Szybko\u015B\u0107:");
		GroupLayout gl_panelAnim = new GroupLayout(panelAnim);
		gl_panelAnim.setHorizontalGroup(
			gl_panelAnim.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAnim.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panelAnim.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSzybko)
						.addComponent(btnApply)
						.addComponent(sliderAnimSpeed, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelAnim.setVerticalGroup(
			gl_panelAnim.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAnim.createSequentialGroup()
					.addGap(27)
					.addComponent(lblSzybko)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sliderAnimSpeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
					.addComponent(btnApply)
					.addGap(20))
		);
		panelAnim.setLayout(gl_panelAnim);
		
		///////////////////////////////////////////////////
		// Pasek narzêdzi
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		
		contentPanel.add(toolBar, BorderLayout.NORTH);
		
		btnNewMap = new ToolBarButton();
		btnNewMap.setIcon(new ImageIcon(Window.class.getResource("/windowBuilder/resources/New.gif")));
		toolBar.add(btnNewMap);
		
		btnSaveMap = new ToolBarButton();
		btnSaveMap.setIcon(new ImageIcon(Window.class.getResource("/windowBuilder/resources/Save.gif")));
		toolBar.add(btnSaveMap);
		
		btnLoadMap = new ToolBarButton();
		btnLoadMap.setIcon(new ImageIcon(Window.class.getResource("/windowBuilder/resources/Load.gif")));
		toolBar.add(btnLoadMap);
		
		toolBar.addSeparator();
		
		btnExit = new ToolBarButton();
		btnExit.setIcon(new ImageIcon(Window.class.getResource("/windowBuilder/resources/Exit.gif")));
		toolBar.add(btnExit);
		
		GLpanel = new JPanel();
		contentPanel.add(GLpanel, BorderLayout.CENTER);
	}

	
	
	///////////////////////////////////////////////////////////////////
	// Metoda zawieraj¹ca kod tworz¹cy event'y komponetów
	///////////////////////////////////////////////////////////////////
	
	private void createEvents() {
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				glcanvas.setSize(GLpanel.getSize());
			}
		});
	}
}
