package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.imageio.ImageIO;
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
import java.awt.Component;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
	private ToolBarToggleButton btnObst;
	private ToolBarToggleButton btnDelObst;
	private ToolBarToggleButton btnStart;
	private ToolBarToggleButton btnEnd;
	private ToolBarButton btnHelp;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel panelSafe;
	private JLabel label;
	private JComboBox comboBox;
	private ToolBarButton btnSaveScreen;
	private ToolBarButton btnCopyLayer;
	private ToolBarButton btnPasteLayer;
	private JCheckBox cboxAnimation;
	
	/**
	 * G��wna aplikacja.
	 */
	
	public WindowMain(Render render) {
		super("Optymalna �cie�ka na mapie rastrowej w 3D");
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
		GLpanel.add(this.render.getGlcanvas(), BorderLayout.CENTER);
		createEvents();
		initLayerSpinner();
		
		//wpi�cie okna opengl do JPanel
		//
	}

	/////////////////////////////////////////////////////////////////////
	// Metoda zawieraj�ca kod tworz�cy komponenty oraz ich inicjalizacje
	/////////////////////////////////////////////////////////////////////
	
	private void initComponents() {
		
		// usatwienie ToolTip oraz JPopupMenu jako komponent�w HeavyWeight
		// naprawia problem chowaj�cego si� menu i podpowiedzi przycisk�w pod glcanvas
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false); // for tooltips
		JPopupMenu.setDefaultLightWeightPopupEnabled(false); // for menus and pop-ups
		
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
		panelMap.setAlignmentY(Component.TOP_ALIGNMENT);
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
							.addComponent(spnLayer, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
						.addComponent(rdbtnMapMod)
						.addComponent(rdbtnPreview))
					.addContainerGap())
		);
		gl_panelMap.setVerticalGroup(
			gl_panelMap.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelMap.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(rdbtnPreview)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnMapMod)
					.addGap(18)
					.addGroup(gl_panelMap.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWarstwa)
						.addComponent(spnLayer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panelMap.setLayout(gl_panelMap);
		
		///////////////////////////////////////////////////
		// Panel algorytm�w
		
		JPanel panelAlg = new JPanel();
		panelAlg.setAlignmentY(Component.TOP_ALIGNMENT);
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
		
		panelSafe = new JPanel();
		panelSafe.setAlignmentY(Component.TOP_ALIGNMENT);
		panelSafe.setBorder(new TitledBorder(null, "Strefa bezpiecze\u0144stwa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelOptions.add(panelSafe);
		
		label = new JLabel("Rodzaj strefy bezpiecze\u0144stwa:");
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Brak", "Wok\u00F3\u0142 robota", "Wok\u00F3\u0142 przeszk\u00F3d"}));
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 3, 1));
		
		JLabel lblWielkoStrefy = new JLabel("Wielko\u015B\u0107 strefy:");
		GroupLayout gl_panelSafe = new GroupLayout(panelSafe);
		gl_panelSafe.setHorizontalGroup(
			gl_panelSafe.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSafe.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelSafe.createParallelGroup(Alignment.LEADING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox, 0, 207, Short.MAX_VALUE)
						.addGroup(gl_panelSafe.createSequentialGroup()
							.addComponent(lblWielkoStrefy)
							.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panelSafe.setVerticalGroup(
			gl_panelSafe.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSafe.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelSafe.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWielkoStrefy)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(103, Short.MAX_VALUE))
		);
		panelSafe.setLayout(gl_panelSafe);
		
		///////////////////////////////////////////////////
		// Panel animacji
		
		JPanel panelAnim = new JPanel();
		panelAnim.setBorder(new TitledBorder(null, "Animacja", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelOptions.add(panelAnim);
		
		btnApply = new JButton("Wykonaj algorytm");
		
		sliderAnimSpeed = new JSlider();
		sliderAnimSpeed.setMaximum(105);
		sliderAnimSpeed.setValue(55);
		sliderAnimSpeed.setMinimum(5);
		sliderAnimSpeed.setToolTipText("");
		sliderAnimSpeed.setSnapToTicks(true);
		sliderAnimSpeed.setPaintTicks(true);
		sliderAnimSpeed.setPaintLabels(true);
		sliderAnimSpeed.setMinorTickSpacing(10);
		
		JLabel lblSzybko = new JLabel("Szybko\u015B\u0107:");
		
		cboxAnimation = new JCheckBox("W\u0142\u0105cz animacje");
		cboxAnimation.setSelected(true);
		GroupLayout gl_panelAnim = new GroupLayout(panelAnim);
		gl_panelAnim.setHorizontalGroup(
			gl_panelAnim.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAnim.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelAnim.createParallelGroup(Alignment.LEADING)
						.addComponent(btnApply, Alignment.TRAILING)
						.addComponent(sliderAnimSpeed, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
						.addComponent(lblSzybko)
						.addComponent(cboxAnimation))
					.addContainerGap())
		);
		gl_panelAnim.setVerticalGroup(
			gl_panelAnim.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAnim.createSequentialGroup()
					.addGap(8)
					.addComponent(cboxAnimation)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblSzybko)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(sliderAnimSpeed, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnApply)
					.addGap(53))
		);
		panelAnim.setLayout(gl_panelAnim);
		
		GLpanel = new JPanel();
		contentPanel.add(GLpanel, BorderLayout.CENTER);
		
		///////////////////////////////////////////////////
		// Pasek narz�dzi
		
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
		
		btnCopyLayer = new ToolBarButton();
		btnCopyLayer.setToolTipText("Kopiuj modyfikowan� warstw�");
		btnPasteLayer = new ToolBarButton();
		btnPasteLayer.setToolTipText("Wklej modyfikowan� warstw�");
		
		btnSaveScreen = new ToolBarButton();
		btnSaveScreen.setToolTipText("Zapisz widok");
		btnHelp = new ToolBarButton();
		btnHelp.setToolTipText("Pomoc");
		btnExit = new ToolBarButton();
		btnExit.setToolTipText("Zamknij program");
		
		btnObst = new ToolBarToggleButton();
		buttonGroup.add(btnObst);
		btnObst.setToolTipText("Rysuj przeszkod�");
		btnDelObst = new ToolBarToggleButton();
		buttonGroup.add(btnDelObst);
		btnDelObst.setToolTipText("Usu� przeszkod�");
		btnStart = new ToolBarToggleButton();
		buttonGroup.add(btnStart);
		btnStart.setToolTipText("Zaznacz punkt startowy");
		btnEnd = new ToolBarToggleButton();
		buttonGroup.add(btnEnd);
		btnEnd.setToolTipText("Zaznacz punkt ko�cowy");
		
		buttonGroup.setSelected(btnObst.getModel(), true);
		
		btnNewMap.setIcon(new ImageIcon("toolbar_icons/new.png"));
		btnSaveMap.setIcon(new ImageIcon("toolbar_icons/save.png"));
		btnLoadMap.setIcon(new ImageIcon("toolbar_icons/open.png"));
		
		btnObst.setIcon(new ImageIcon("toolbar_icons/obst.png"));
		btnDelObst.setIcon(new ImageIcon("toolbar_icons/delobst.png"));
		btnStart.setIcon(new ImageIcon("toolbar_icons/start.png"));
		btnEnd.setIcon(new ImageIcon("toolbar_icons/finish.png"));
		
		btnCopyLayer.setIcon(new ImageIcon("toolbar_icons/copy.png"));
		btnPasteLayer.setIcon(new ImageIcon("toolbar_icons/paste.png"));
		
		btnSaveScreen.setIcon(new ImageIcon("toolbar_icons/camera.png"));
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
		toolBar.add(btnPasteLayer);
		toolBar.add(btnCopyLayer);
		toolBar.addSeparator();
		toolBar.add(btnSaveScreen);
		toolBar.add(btnHelp);
		toolBar.add(btnExit);
		
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
		double pCenterY = (lengthZ - sizeRaster)/2;
		double pCenterZ = (lengthY - sizeRaster)/2;
		
		//usatawienie kamery w �rodku mapy
		if (side == 0) {
			double pPosZ = lengthY*2.5 - sizeRaster;
			render.getCamera().setPointCenter(new Point3d(pCenterX, pCenterY, pCenterZ));
			render.getCamera().setPointPos(new Point3d(pCenterX, Math.tan(Math.toRadians(13.0))*pPosZ, pPosZ ));
		} else {
			render.getCamera().setPointCenter(new Point3d(pCenterX, 0.0, pCenterZ));
			render.getCamera().setPointPos(new Point3d(pCenterX, (lengthZ - sizeRaster) + 2.6*sizeRaster, pCenterZ+0.02 ));
		}
	}

	
	public int getSelectedToolbarButton() {
		if (buttonGroup.isSelected(btnObst.getModel()))
			return 0;
		if (buttonGroup.isSelected(btnDelObst.getModel()))
			return 1;
		if (buttonGroup.isSelected(btnStart.getModel()))
			return 2;
		if (buttonGroup.isSelected(btnEnd.getModel()))
			return 3;
		return -1;
	}
	
	///////////////////////////////////////////////////////////////////
	// Metoda zawieraj�ca kod tworz�cy event'y komponet�w
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
		
		btnSaveScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG (*.png)", "PNG", "png");
				fc.setFileFilter(filter);
				
				int returnVal = fc.showSaveDialog(windowMain);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	String filePath = fc.getSelectedFile().getPath();
			    	//This is where a real application would open the file.
			    	if (fc.getFileFilter().getDescription() == "PNG (*.png)" && !filePath.endsWith(".png"))
			    		filePath += ".png";
			    	
			    	System.out.println("Saving: " + filePath);
			    	render.saveImage(filePath);
				} else {
			    	System.out.println("Save command cancelled by user.");
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
				render.getCamera().clearPrevCamera();
			}
		});
		
		rdbtnMapMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spnLayer.setEnabled(true);
				render.setMapCreation(true);
				render.getRenderMap().resetPath();
				if (!render.getCamera().isPrevCamera())
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
		
		btnObst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				render.getEditionMap().setSelectedOption(getSelectedToolbarButton());
			}
		});
		
		btnDelObst.addActionListener(btnObst.getActionListeners()[0]);
		btnStart.addActionListener(btnObst.getActionListeners()[0]);
		btnEnd.addActionListener(btnObst.getActionListeners()[0]);
		
		
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				render.setAnimation(cboxAnimation.isSelected());
				render.getRenderMap().setSpeedAnimation(sliderAnimSpeed.getValue());
				render.getRenderMap().resetPath();
				boolean isChebyshev = false;
				if (cbMetrics.getSelectedIndex() == 1)
					isChebyshev = true;
				render.getRenderMap().performAStar(isChebyshev);
			}
		});
		
		sliderAnimSpeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				render.getRenderMap().setSpeedAnimation(sliderAnimSpeed.getValue());
			}
		});
	}

	public JPanel getGLpanel() {
		return GLpanel;
	}
}
