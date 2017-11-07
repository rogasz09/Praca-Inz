package optimumPath.window;

import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import javax.swing.UIManager;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import optimumPath.frame.*;
import optimumPath.opengl.*;
import optimumPath.object.Map;

public class WindowMain extends JFrame {
	
	/**
	 * Auto generated
	 */
	private static final long serialVersionUID = -267010437272070514L;
	
	private Render render;
	private JMenuBar menuBar;
	private JComboBox cbAlgorithm, cbMetrics;
	private JSlider sliderAnimSpeed;
	private JButton btnApply;
	private ToolBarButton btnNewMap, btnSaveMap, btnLoadMap, btnExit;
	private JPanel GLpanel;
	
	/**
	 * G³ówna aplikacja.
	 */
	
	public WindowMain(Render render) {
		super("Optymalna œcie¿ka na mapie rastrowej");
		setResizable(false);
		setBounds(100, 100, 922, 669);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.render = render;
		initComponents();
		createEvents();
		
		//wpiêcie okna opengl do JPanel
		GLpanel.add(this.render.getGlcanvas(), BorderLayout.CENTER);
	}

	/////////////////////////////////////////////////////////////////////
	// Metoda zawieraj¹ca kod tworz¹cy komponenty oraz ich inicjalizacje
	/////////////////////////////////////////////////////////////////////
	
	private void initComponents() {
		
		///////////////////////////////////////////////////
		// Menu
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
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
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelOptions = new JPanel();
		panelOptions.setBorder(null);
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
		btnSaveMap = new ToolBarButton();
		btnLoadMap = new ToolBarButton();
		btnExit = new ToolBarButton();
		
		btnNewMap.setIcon(new ImageIcon("toolbar_icons/New.gif"));
		btnSaveMap.setIcon(new ImageIcon("toolbar_icons/Save.gif"));
		btnLoadMap.setIcon(new ImageIcon("toolbar_icons/Load.gif"));
		btnExit.setIcon(new ImageIcon("toolbar_icons/Exit.gif"));
		
		toolBar.add(btnNewMap);
		toolBar.add(btnSaveMap);
		toolBar.add(btnLoadMap);
		toolBar.addSeparator();
		toolBar.add(btnExit);
		
		GLpanel = new JPanel();
		contentPanel.add(GLpanel, BorderLayout.CENTER);
	}

	
	
	///////////////////////////////////////////////////////////////////
	// Metoda zawieraj¹ca kod tworz¹cy event'y komponetów
	///////////////////////////////////////////////////////////////////
	
	private void createEvents() {
		btnNewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sizeX = 10;
				int sizeY = 10;
				int sizeZ = 4;
				
				int testMap[][][] = 
					{{{1, 1, 1, 0, 1, 1, 0, 0, 0, 1},
					  {1, 0, 1, 0, 1, 1, 0, 0, 0, 0},
				      {1, 1, 1, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 1, 1}},
							
					 {{1, 0, 1, 0, 1, 1, 0, 0, 0, 0},
					  {0, 1, 0, 0, 1, 1, 0, 0, 0, 0},
					  {1, 0, 1, 0, 1, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0}},
					 
				     {{0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 1, 1, 0, 0, 0, 0}},
				     
				     {{0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
				      {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
					  {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					  {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					  {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
					  {1, 0, 0, 0, 0, 0, 0, 0, 0, 1}}};
				
				render.getRenderMap().setSize(sizeX, sizeY, sizeZ);
				render.getRenderMap().initMap();
				
				for(int z = 0; z < sizeZ; z++)
					for(int y = 0; y < sizeY; y++)
						for(int x = 0; x < sizeX; x++) {
							if (testMap[z][y][x] == 1)
								render.getRenderMap().setRaster(x, y, z, Map.Raster.OBSTACLE);;
						}
							
				render.getRenderMap().makeShiftList();
			}
		});
	}

	public JPanel getGLpanel() {
		return GLpanel;
	}
	
	
}
