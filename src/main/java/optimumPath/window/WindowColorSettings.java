package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import optimumPath.frame.CustomPreviewPanel;
import optimumPath.object.MaterialsList;

import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class WindowColorSettings extends JDialog implements ActionListener{

	private static final long serialVersionUID = 7745791835290184068L;
	
	private final JPanel contentPanel = new JPanel();
	private MaterialsList materials;
	
	private JPanel colorObstacle, colorForbidden;
	private JPanel colorStart, colorEnd, colorPath;
	private JPanel colorOpen, colorClosed;
	private JButton btnObstacle, btnForbidden;
	private JButton btnStart, btnEnd, btnPath;
	private JButton btnOpen, btnClosed;
	private JLabel alfaObstacle, alfaForbidden;
	private JLabel alfaStart, alfaEnd, alfaPath;
	private JLabel alfaOpen, alfaClosed;
	
	private Color cObstacle, cForbidden;
	private Color cStart, cEnd, cPath;
	private Color cOpen, cClosed;
	private JPanel panel_1;
	private JRadioButton rbBlack;
	private JRadioButton rbWhite;
	private JLabel lblKolorTa;
	private final ButtonGroup bgBlackWhite = new ButtonGroup();

	/**
	 * Create the dialog.
	 */
	
	public WindowColorSettings(MaterialsList materials) {
		this.materials = materials;
		
		setResizable(false);
		setTitle("Ustawienia kolor\u00F3w");
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/icon.png"));
		setBounds(100, 100, 475, 398);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Podstawowe kolory", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			
			JLabel lblKolorPrzeszkd = new JLabel("Przeszkoda: ");
			JLabel lblNewLabel = new JLabel("Punkt startowy: ");
			JLabel lblKolorPunktuKoniec = new JLabel("Punkt ko\u0144cowy: ");
			JLabel lblKolorRastraZabronionego = new JLabel("Raster zabroniony: ");
			
			colorObstacle = new JPanel();
			colorObstacle.setForeground(Color.BLACK);
			colorObstacle.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorObstacle.setBackground(Color.WHITE);
			colorForbidden = new JPanel();
			colorForbidden.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorForbidden.setBackground(Color.WHITE);
			colorStart = new JPanel();
			colorStart.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorStart.setBackground(Color.WHITE);
			colorEnd = new JPanel();
			colorEnd.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorEnd.setBackground(Color.WHITE);
			colorPath = new JPanel();
			colorPath.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorPath.setBackground(Color.WHITE);
			
			JLabel lblKolorcieki = new JLabel("\u015Acie\u017Cka: ");
			
			JLabel lblNewLabel_1 = new JLabel("Kolor");
			panel.setLayout(new MigLayout("", "[153px][67px][99px][]", "[16px][2][25px][25px][25px][25px][25px]"));
			
			JLabel lblPrzezroczysto = new JLabel("Przezroczysto\u015B\u0107");
			panel.add(lblPrzezroczysto, "cell 2 0,alignx center");
			
			alfaObstacle = new JLabel("0.0");
			panel.add(alfaObstacle, "cell 2 2,alignx center");
			
			btnObstacle = new JButton("Zmie\u0144 kolor");
			panel.add(btnObstacle, "cell 3 2,alignx left,aligny top");
			
			alfaForbidden = new JLabel("0.0");
			panel.add(alfaForbidden, "cell 2 3,alignx center");
			btnForbidden = new JButton("Zmie\u0144 kolor");
			panel.add(btnForbidden, "cell 3 3,alignx left,aligny top");
			panel.add(lblNewLabel, "cell 0 4,alignx right,aligny center");
			panel.add(lblKolorRastraZabronionego, "cell 0 3,alignx right,aligny center");
			panel.add(lblKolorPrzeszkd, "cell 0 2,alignx right,aligny center");
			
			alfaStart = new JLabel("0.0");
			panel.add(alfaStart, "cell 2 4,alignx center");
			btnStart = new JButton("Zmie\u0144 kolor");
			panel.add(btnStart, "cell 3 4,alignx left,aligny top");
			panel.add(lblKolorPunktuKoniec, "cell 0 5,alignx right,aligny center");
			
			alfaEnd = new JLabel("0.0");
			panel.add(alfaEnd, "cell 2 5,alignx center");
			btnEnd = new JButton("Zmie\u0144 kolor");
			panel.add(btnEnd, "cell 3 5,alignx left,aligny top");
			panel.add(lblKolorcieki, "cell 0 6,alignx right,aligny center");
			panel.add(lblNewLabel_1, "cell 1 0,alignx center,aligny top");
			panel.add(colorObstacle, "cell 1 2,grow");
			panel.add(colorForbidden, "cell 1 3,grow");
			panel.add(colorStart, "cell 1 4,grow");
			panel.add(colorEnd, "cell 1 5,grow");
			panel.add(colorPath, "cell 1 6,grow");
			
			alfaPath = new JLabel("0.0");
			panel.add(alfaPath, "cell 2 6,alignx center");
			btnPath = new JButton("Zmie\u0144 kolor");
			panel.add(btnPath, "cell 3 6,alignx left,aligny top");
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Kolory dla algorytm\u00F3w", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.SOUTH);
			
			
			JLabel lblKolorOtwartych = new JLabel("Rastry otwarte: ");
			panel.setLayout(new MigLayout("", "[153px][67px][99px][]", "[][2][25px][25px]"));
			
			JLabel label = new JLabel("Kolor");
			panel.add(label, "cell 1 0,alignx center");
			
			JLabel label_1 = new JLabel("Przezroczysto\u015B\u0107");
			panel.add(label_1, "cell 2 0,alignx center");
			colorOpen = new JPanel();
			colorOpen.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorOpen.setBackground(Color.WHITE);
			panel.add(colorOpen, "cell 1 2,grow");
			
			alfaOpen = new JLabel("0.0");
			panel.add(alfaOpen, "cell 2 2,alignx center");
			btnOpen = new JButton("Zmie\u0144 kolor");
			panel.add(btnOpen, "cell 3 2,alignx left,aligny top");
			
			JLabel lblKolorZamknitych = new JLabel("Kolor zamkni\u0119tych: ");
			panel.add(lblKolorZamknitych, "cell 0 3,alignx right,aligny center");
			
			colorClosed = new JPanel();
			colorClosed.setBorder(new LineBorder(new Color(0, 0, 0)));
			colorClosed.setBackground(Color.WHITE);
			panel.add(colorClosed, "cell 1 3,grow");
			
			alfaClosed = new JLabel("0.0");
			panel.add(alfaClosed, "cell 2 3,alignx center");
			btnClosed = new JButton("Zmie\u0144 kolor");
			panel.add(btnClosed, "cell 3 3,alignx left,aligny bottom");
			panel.add(lblKolorOtwartych, "cell 0 2,alignx right,aligny center");
		}
		
		panel_1 = new JPanel();
		contentPanel.add(panel_1, BorderLayout.NORTH);
		
		lblKolorTa = new JLabel("Kolor t\u0142a:  ");
		panel_1.add(lblKolorTa);
		
		rbBlack = new JRadioButton("Czarny");
		bgBlackWhite.add(rbBlack);
		panel_1.add(rbBlack);
		
		rbWhite = new JRadioButton("Bia\u0142y");
		bgBlackWhite.add(rbWhite);
		panel_1.add(rbWhite);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Anuluj");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				JButton resetButton = new JButton("Domyœlne");
				resetButton.addActionListener(this);
				resetButton.setActionCommand("Default");
				buttonPane.add(resetButton);
			}
		}
		
		createEvents();
		getColors();
		getBackgroundColor();
	}
	
	public void createEvents() {
		btnObstacle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cObstacle = showColorChooser(cObstacle);
				setComponents();
				setAlfas();
			}
		});
		
		btnForbidden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cForbidden = showColorChooser(cForbidden);
				setComponents();
				setAlfas();
			}
		});
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cStart = showColorChooser(cStart);
				setComponents();
				setAlfas();
			}
		});
		
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cEnd = showColorChooser(cEnd);
				setComponents();
				setAlfas();
			}
		});
		
		btnPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPath = showColorChooser(cPath);
				setComponents();
				setAlfas();
			}
		});
		
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cOpen = showColorChooser(cOpen);
				setComponents();
				setAlfas();
			}
		});
		
		btnClosed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cClosed = showColorChooser(cClosed);
				setComponents();
				setAlfas();
			}
		});
	}
	
	public void setComponents() {
		colorObstacle.setBackground(new Color(cObstacle.getRed(), cObstacle.getGreen(), cObstacle.getBlue()));
		colorForbidden.setBackground(new Color(cForbidden.getRed(), cForbidden.getGreen(), cForbidden.getBlue()));
		colorStart.setBackground(new Color(cStart.getRed(), cStart.getGreen(), cStart.getBlue()));
		colorEnd.setBackground(new Color(cEnd.getRed(), cEnd.getGreen(), cEnd.getBlue()));
		colorPath.setBackground(new Color(cPath.getRed(), cPath.getGreen(), cPath.getBlue()));
		colorOpen.setBackground(new Color(cOpen.getRed(), cOpen.getGreen(), cOpen.getBlue()));
		colorClosed.setBackground(new Color(cClosed.getRed(), cClosed.getGreen(), cClosed.getBlue()));
	}
	
	public void setAlfas() {
		float f_AlfaObst = ((float)cObstacle.getAlpha()/255.0f) * 100.0f;
		float f_AlfaForbid = ((float)cForbidden.getAlpha()/255.0f) * 100.0f;
		float f_AlfaStart = ((float)cStart.getAlpha()/255.0f) * 100.0f;
		float f_AlfaEnd = ((float)cEnd.getAlpha()/255.0f) * 100.0f;
		float f_AlfaPath = ((float)cPath.getAlpha()/255.0f) * 100.0f;
		float f_AlfaOpen = ((float)cOpen.getAlpha()/255.0f) * 100.0f;
		float f_AlfaClosed = ((float)cClosed.getAlpha()/255.0f) * 100.0f;
		
		alfaObstacle.setText(((Float)(Math.round(f_AlfaObst*10.0f)/10.0f)).toString() + "%");
		alfaForbidden.setText(((Float)(Math.round(f_AlfaForbid*10.0f)/10.0f)).toString() + "%");
		alfaStart.setText(((Float)(Math.round(f_AlfaStart*10.0f)/10.0f)).toString() + "%");
		alfaEnd.setText(((Float)(Math.round(f_AlfaEnd*10.0f)/10.0f)).toString() + "%");
		alfaPath.setText(((Float)(Math.round(f_AlfaPath*10.0f)/10.0f)).toString() + "%");
		alfaOpen.setText(((Float)(Math.round(f_AlfaOpen*10.0f)/10.0f)).toString() + "%");
		alfaClosed.setText(((Float)(Math.round(f_AlfaClosed*10.0f)/10.0f)).toString() + "%");
	}
	
	public void getColors() {
		cObstacle = materials.getMatObstacle().getDiffuseColor();
		cForbidden = materials.getMatForbidden().getDiffuseColor();
		cStart = materials.getMatStart().getDiffuseColor();
		cEnd = materials.getMatEnd().getDiffuseColor();
		cPath = materials.getMatPath().getDiffuseColor();
		cOpen = materials.getMatOpen().getDiffuseColor();
		cClosed = materials.getMatClosed().getDiffuseColor();
		
		setComponents();
		setAlfas();
	}
	
	public void getBackgroundColor() {
		if (isSameColor(materials.getBackground(), Color.BLACK))
			rbBlack.setSelected(true);
		else
			rbWhite.setSelected(true);
	}
	
	public void setBackgroundColor() {
		if (rbBlack.isSelected())
			materials.setBackground(Color.BLACK);
		else
			materials.setBackground(Color.WHITE);
	}
	
	public void setColors() {
		materials.getMatObstacle().setDiffuse(cObstacle);
		materials.getMatForbidden().setDiffuse(cForbidden);
		materials.getMatStart().setDiffuse(cStart);
		materials.getMatEnd().setDiffuse(cEnd);
		materials.getMatPath().setDiffuse(cPath);
		materials.getMatOpen().setDiffuse(cOpen);
		materials.getMatClosed().setDiffuse(cClosed);
		
		setComponents();
		setAlfas();
	}
	
	public boolean isSameColor(Color color1, Color color2) {
		if (color1.getRed() == color2.getRed())
			if (color1.getGreen() == color2.getGreen())
				if (color1.getBlue() == color2.getBlue())
					return true;
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Cancel")) {
			this.dispose();
		}
		if (action.equals("OK")) {
			setColors();
			setBackgroundColor();
			this.dispose();
		}
		if (action.equals("Default")) {
			materials.setDefaultMaterials();
			getColors();
			getBackgroundColor();
		}
	}
	
	
	///////////////////////////////////////////////////
	// Wlasne okno jcolorchooser
	public Color showColorChooser(Color color) {
		JColorChooser cc = new JColorChooser();
		final CustomPreviewPanel pre = new CustomPreviewPanel(cc);
	    cc.setPreviewPanel(pre);
	    ColorSelectionModel model = cc.getSelectionModel();
	    model.addChangeListener(new ChangeListener() {
	      public void stateChanged(ChangeEvent evt) {
	        ColorSelectionModel model = (ColorSelectionModel) evt.getSource();
	        Color selColor = model.getSelectedColor();
	        pre.setCurColor(new Color(selColor.getRed(), selColor.getGreen(), selColor.getBlue()));
	      }
	    });
	    
		cc.setColor(color);
        AbstractColorChooserPanel[] panels = cc.getChooserPanels();

        JPanel p = new JPanel();
        panels[3].setBorder( new TitledBorder(panels[3].getDisplayName()));
        p.add(panels[3]);

        JPanel gui = new JPanel(new BorderLayout(2,2));
        gui.add(p, BorderLayout.CENTER);
        gui.add(cc.getPreviewPanel(), BorderLayout.SOUTH);
        
        JDialog colorRGB = new JDialog();
        ActionListener dialogListener = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String action = e.getActionCommand();
        		if (action.equals("Cancel")) {
        			colorRGB.dispose();
        		}
        		if (action.equals("OK")) {
        			colorRGB.dispose();
        		}
    		}
        };
        
        colorRGB.getContentPane().setLayout(new BorderLayout());
        gui.setLayout(new FlowLayout());
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        colorRGB.getContentPane().add(gui, BorderLayout.CENTER);
        colorRGB.getContentPane().add(gui, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			colorRGB.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(dialogListener);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				colorRGB.getRootPane().setDefaultButton(okButton);
			}
		}
		
        colorRGB.setBounds(100, 100, 650, 400);
        colorRGB.setTitle("Wybór koloru");
        
        colorRGB.setResizable(false);
        colorRGB.setModal(true);
        colorRGB.setVisible(true);
        
		return cc.getColor();
	}
}
