package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;


import optimumPath.frame.*;
import optimumPath.opengl.*;
import optimumPath.object.Map;
import optimumPath.common.Point3d;
import optimumPath.JSON.*;


public class WindowMain extends JFrame {
	
	/**
	 * Auto generated
	 */
	private static final long serialVersionUID = -267010437272070514L;
	
	private Render render;
	private JsonWriteRead json;
	
	private JMenuBar menuBar;
	private JSlider sliderAnimSpeed;
	private JButton btnApply;
	private JPanel GLpanel;
	private JSpinner spnLayer;
	private JRadioButton rdbtnPreview, rdbtnMapMod;
	private JComboBox cbAlgorithm, cbMetrics;
	private ToolBarButton btnNewMap, btnSaveMap, btnLoadMap, btnExit;
	private JMenuItem mntmExit, mntmLoadMap, mntmSaveMap, mntmNewMap;
	private JMenuItem mntmDrawObstacle, mntmCheckStartPoint, mntmCheckStopPoint;
	
	//Create a file chooser
	final private JFileChooser fc;
	private JFrame windowMain = this;
	private JToggleButton btnObst;
	private JToggleButton btnDelObst;
	private JToggleButton btnStart;
	private JToggleButton btnEnd;
	private ToolBarButton btnHelp;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	/**
	 * G³ówna aplikacja.
	 */
	
	public WindowMain(Render render) {
		super("Optymalna œcie¿ka na mapie rastrowej w 3D");
		setResizable(false);
		setBounds(100, 100, 922, 767);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.render = render;
		this.json = new JsonWriteRead();
		this.fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "JSON", "json");
		fc.setFileFilter(filter);
		setCameraToCenter(0);
		
		initComponents();
		createEvents();
		initLayerSpinner();
		
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
		
		mntmNewMap = new JMenuItem("Nowa mapa");
		mnNewMenu.add(mntmNewMap);
		
		mntmSaveMap = new JMenuItem("Zapisz mape");
		mnNewMenu.add(mntmSaveMap);
		
		mntmLoadMap = new JMenuItem("Wczytaj mape");
		mnNewMenu.add(mntmLoadMap);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		mntmExit = new JMenuItem("Zamknij");
		mnNewMenu.add(mntmExit);
		
		JMenu mnEdycja = new JMenu("Edycja");
		menuBar.add(mnEdycja);
		
		mntmDrawObstacle = new JMenuItem("Rysuj przeszkod\u0119");
		mnEdycja.add(mntmDrawObstacle);
		
		mntmCheckStartPoint = new JMenuItem("Zaznacz punkt startowy");
		mnEdycja.add(mntmCheckStartPoint);
		
		mntmCheckStopPoint = new JMenuItem("Zaznacz punkt ko\u0144cowy");
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
		
		rdbtnPreview = new JRadioButton("Podgl\u0105d");
		rdbtnPreview.setSelected(true);
		rdbtnMapMod = new JRadioButton("Modyfikacja mapy");
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnMapMod);
		group.add(rdbtnPreview);
		
		spnLayer = new JSpinner();
		spnLayer.setEnabled(false);
		
		JLabel lblWarstwa = new JLabel("Warstwa:");
		
		
		GroupLayout gl_panelMap = new GroupLayout(panelMap);
		gl_panelMap.setHorizontalGroup(
			gl_panelMap.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMap.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panelMap.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMap.createSequentialGroup()
							.addComponent(lblWarstwa)
							.addGap(18)
							.addComponent(spnLayer, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
						.addComponent(rdbtnMapMod)
						.addComponent(rdbtnPreview))
					.addContainerGap())
		);
		gl_panelMap.setVerticalGroup(
			gl_panelMap.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelMap.createSequentialGroup()
					.addContainerGap(10, Short.MAX_VALUE)
					.addComponent(rdbtnPreview)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnMapMod)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMap.createParallelGroup(Alignment.BASELINE)
						.addComponent(spnLayer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblWarstwa))
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
		
		//Dimension toolBarButtonSize = new Dimension(30, 30);
		JPanel panelTool = new JPanel();
		contentPanel.add(panelTool, BorderLayout.NORTH);
		panelTool.setLayout(new BorderLayout(0, 0));
		//panelTool.setMinimumSize(toolBarButtonSize);
		//panelTool.setPreferredSize(toolBarButtonSize);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
			
		panelTool.add(toolBar);
		
		btnNewMap = new ToolBarButton();
		btnNewMap.setToolTipText("Nowa mapa");
		btnSaveMap = new ToolBarButton();
		btnSaveMap.setToolTipText("Zapisz mape");
		btnLoadMap = new ToolBarButton();
		btnLoadMap.setToolTipText("Wczytaj mape");
		
		btnHelp = new ToolBarButton();
		btnHelp.setToolTipText("Pomoc");
		btnExit = new ToolBarButton();
		btnExit.setToolTipText("Zamknij program");
		
		btnObst = new ToolBarToggleButton();
		btnObst.setSelected(true);
		buttonGroup.add(btnObst);
		btnObst.setToolTipText("Rysuj przeszkodê");
		btnDelObst = new ToolBarToggleButton();
		buttonGroup.add(btnDelObst);
		btnDelObst.setToolTipText("Usuñ przeszkodê");
		btnStart = new ToolBarToggleButton();
		buttonGroup.add(btnStart);
		btnStart.setToolTipText("Zaznacz punkt startowy");
		btnEnd = new ToolBarToggleButton();
		buttonGroup.add(btnEnd);
		btnEnd.setToolTipText("Zaznacz punkt koñcowy");
		
		btnNewMap.setIcon(new ImageIcon("toolbar_icons/new.png"));
		btnSaveMap.setIcon(new ImageIcon("toolbar_icons/save.png"));
		btnLoadMap.setIcon(new ImageIcon("toolbar_icons/open.png"));
		
		btnObst.setIcon(new ImageIcon("toolbar_icons/obst.png"));
		btnDelObst.setIcon(new ImageIcon("toolbar_icons/delobst.png"));
		btnStart.setIcon(new ImageIcon("toolbar_icons/start.png"));
		btnEnd.setIcon(new ImageIcon("toolbar_icons/finish.png"));
		
		btnHelp.setIcon(new ImageIcon("toolbar_icons/help.png"));
		btnExit.setIcon(new ImageIcon("toolbar_icons/exit.png"));
		
		toolBar.add(btnNewMap);
		toolBar.add(btnSaveMap);
		toolBar.add(btnLoadMap);
		toolBar.addSeparator();
		toolBar.add(btnObst);
		toolBar.add(btnDelObst);
		toolBar.add(btnStart);
		toolBar.add(btnEnd);
		toolBar.addSeparator();
		toolBar.add(btnHelp);
		toolBar.add(btnExit);
		
		GLpanel = new JPanel();
		contentPanel.add(GLpanel, BorderLayout.CENTER);
	}
	
	public void initLayerSpinner() {
		int maxLayer = render.getRenderMap().getSizeZ() - 1;
		spnLayer.setModel(new SpinnerNumberModel(0, 0, maxLayer, 1));
		//spnLayer.setValue(Integer.valueOf(0));
	}

	public void setOffsetMap() {
		int layer = ((Integer)spnLayer.getValue()).intValue();
		render.setOffsetLayer(layer);
	}
	
	
	public void setCameraToCenter(int side) {
		double sizeRaster = render.getRenderMap().getSizeRaster();
		
		double lengthX = render.getRenderMap().getSizeX()*sizeRaster;
		double lengthY = render.getRenderMap().getSizeY()*sizeRaster;
		double lengthZ = render.getRenderMap().getSizeZ()*sizeRaster;
		
		double pCenterX = (lengthX - sizeRaster)/2;
		double pCenterY = 0.0;
		double pCenterZ = (lengthY - sizeRaster)/2;
		
		System.out.println("w: " + Integer.toString(render.getGlcanvas().getWidth()) + " h: " + Integer.toString(render.getGlcanvas().getHeight()));
		System.out.println("Test" + Double.toString((lengthZ - sizeRaster) + 2.6*sizeRaster));
		System.out.println("Test" + Double.toString(((lengthZ - sizeRaster) + 2.6*sizeRaster)*Math.tan(Math.toRadians(45.0))));
		//usatawienie kamery w œrodku mapy
		render.getCamera().setPointCenter(new Point3d(pCenterX, 0.0, pCenterZ));
		if (side == 0) {
			double pPosZ = lengthY*1.5 - sizeRaster;
			render.getCamera().setPointPos(new Point3d(pCenterX, Math.tan(Math.toRadians(13.0))*pPosZ, pPosZ ));
		} else
			render.getCamera().setPointPos(new Point3d(pCenterX, (lengthZ - sizeRaster) + 2.6*sizeRaster, pCenterZ+0.01 ));
	}
	
	public void createTemplateMap() {
		//---------------------------------
		//kod tymczasowy
		//inicjalizacja mapy
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
		
		for(int z = 0; z < 4; z++)
			for(int y = 0; y < 10; y++)
				for(int x = 0; x < 10; x++) {
					if (testMap[z][y][x] == 1) 
						render.getRenderMap().setRaster(x, y, z, Map.Raster.OBSTACLE);
				}
					
		render.getRenderMap().makeShiftList();
	}
	
	
	///////////////////////////////////////////////////////////////////
	// Metoda zawieraj¹ca kod tworz¹cy event'y komponetów
	///////////////////////////////////////////////////////////////////
	
	private void createEvents() {
		btnNewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WindowNewMap newMap = new WindowNewMap();
				int loactionX = getX() + (getWidth() - newMap.getWidth())/2;
				int loactionY = getY() + (getHeight() - newMap.getHeight())/2;
				newMap.setLocation(loactionX, loactionY);
				newMap.setModal(true);
				newMap.setVisible(true);
				
				if (!newMap.isOk())
					return;
				
				Point3d size = newMap.getSizeMap();
				
				int sizeX = (int)size.getX();
				int sizeY = (int)size.getY();
				int sizeZ = (int)size.getZ();
				
				render.getRenderMap().setSize(sizeX, sizeY, sizeZ);
				render.getRenderMap().initMap();
				setCameraToCenter(0);
				initLayerSpinner();
				createTemplateMap();
			}
		});
		
		btnSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showSaveDialog(windowMain);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	String filePath = fc.getSelectedFile().getPath();
			    	//This is where a real application would open the file.
			    	if (fc.getFileFilter().getDescription() == "JSON Files" && !filePath.endsWith(".JSON"))
			    		filePath += ".JSON";
			    	
			    	System.out.println("Saving: " + filePath);
			    	int outputMap[][][] = render.getRenderMap().rasterMapToIntMap();
			    	int sizeX = render.getRenderMap().getSizeX();
			    	int sizeY = render.getRenderMap().getSizeY();
			    	int sizeZ = render.getRenderMap().getSizeZ();
			    	json.printMap(outputMap, sizeZ, sizeY, sizeX);
			    	json.writeMapToJSON(filePath, outputMap, sizeZ, sizeY, sizeX);
				} else {
			    	System.out.println("Save command cancelled by user.");
			   	}
			}
		});
		
		btnLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(windowMain);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filePath = fc.getSelectedFile().getPath();
			    	//This is where a real application would open the file.
			        System.out.println("Opening: " + filePath);
			        int outputMap[][][] = json.loadMapFromJSON(filePath);
			        int sizeX = json.getSizeXfromJSON();
			        int sizeY = json.getSizeYfromJSON();
			        int sizeZ = json.getSizeZfromJSON();
			        json.printMap(outputMap, sizeZ, sizeY, sizeX);
			        render.getRenderMap().intMapToRasterMap(outputMap, sizeX, sizeY, sizeZ);
			        setCameraToCenter(0);
					initLayerSpinner();
			    } else {
			    	System.out.println("Open command cancelled by user.");
			    }
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		
		mntmNewMap.addActionListener(btnNewMap.getActionListeners()[0]);
		mntmSaveMap.addActionListener(btnSaveMap.getActionListeners()[0]);
		mntmLoadMap.addActionListener(btnLoadMap.getActionListeners()[0]);
		mntmExit.addActionListener(btnExit.getActionListeners()[0]);
		
		rdbtnPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spnLayer.setEnabled(false);
				render.setMapCreation(false);
				render.getCamera().loadPrevCamera();
			}
		});
		
		rdbtnMapMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spnLayer.setEnabled(true);
				render.setMapCreation(true);
				render.getCamera().saveActualCamera();
				setCameraToCenter(1);
			}
		});
		
		spnLayer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				System.out.println("Zmieniono warstwe!");
				setOffsetMap();
			}
		});
	}

	public JPanel getGLpanel() {
		return GLpanel;
	}
}
