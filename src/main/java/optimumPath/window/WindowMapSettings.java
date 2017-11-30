package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.LayoutStyle.ComponentPlacement;

import optimumPath.common.Point3d;


public class WindowMapSettings extends JDialog {

	private static final long serialVersionUID = 6397790310030842569L;
	private final JPanel contentPanel = new JPanel();
	private JSpinner spnSizeX, spnSizeY, spnSizeZ;
	private JSpinner spnRaster;
	private JButton okButton;
	private JButton cancelButton;
	
	private boolean isOk = false;


	/**
	 * Create the dialog.
	 */
	public WindowMapSettings() {
		
		setResizable(false);
		setTitle("Ustawienia Mapy");
		setBounds(100, 100, 298, 301);
		setIconImage(Toolkit.getDefaultToolkit().getImage("toolbar_icons/icon.png"));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblParametryMapa = new JLabel("Parametry mapa:");
		lblParametryMapa.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblRozmiarX = new JLabel("Rozmiar X:");
		JLabel lblRozmiarY = new JLabel("Rozmiar Y:");
		JLabel lblRozmiarZ = new JLabel("Rozmiar Z:");
		JLabel lblRasterSize = new JLabel("Rozmiar rastra:");
		
		spnSizeX = new JSpinner();
		spnSizeX.setModel(new SpinnerNumberModel(10, 3, 50, 1));
		
		spnSizeY = new JSpinner();
		spnSizeY.setModel(new SpinnerNumberModel(10, 3, 50, 1));
		
		spnSizeZ = new JSpinner();
		spnSizeZ.setModel(new SpinnerNumberModel(10, 1, 50, 1));
		
		spnRaster = new JSpinner();
		spnRaster.setModel(new SpinnerNumberModel(1.0, 0.5, 3.0, 0.1));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblParametryMapa)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblRasterSize)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(spnRaster, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblRozmiarY, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblRozmiarX, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblRozmiarZ, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(spnSizeZ, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
								.addComponent(spnSizeY, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
								.addComponent(spnSizeX, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(42, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(19)
					.addComponent(lblParametryMapa)
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRasterSize)
						.addComponent(spnRaster, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRozmiarX)
						.addComponent(spnSizeX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRozmiarY)
						.addComponent(spnSizeY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRozmiarZ)
						.addComponent(spnSizeZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(80, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		createEvents();
	}
	
	private void createEvents() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isOk = false;
				dispose();
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isOk = true;
				dispose();
			}
		});
	}
	
	public double getSizeRaster() {
		Double spnSizeRaster = (Double)spnRaster.getValue();
		return spnSizeRaster.doubleValue();
	}
	
	public Point3d getSizeMap() {
		Integer spnSizeXValue = (Integer)spnSizeX.getValue();
		Integer spnSizeYValue = (Integer)spnSizeY.getValue();
		Integer spnSizeZValue = (Integer)spnSizeZ.getValue();
		
		Point3d size = new Point3d(spnSizeXValue.doubleValue(), spnSizeYValue.doubleValue(), spnSizeZValue.doubleValue());
		return size;
	}

	public boolean isOk() {
		return isOk;
	}

	public JSpinner getSpnSizeX() {
		return spnSizeX;
	}

	public void setSpnSizeX(JSpinner spnSizeX) {
		this.spnSizeX = spnSizeX;
	}

	public JSpinner getSpnSizeY() {
		return spnSizeY;
	}

	public void setSpnSizeY(JSpinner spnSizeY) {
		this.spnSizeY = spnSizeY;
	}

	public JSpinner getSpnSizeZ() {
		return spnSizeZ;
	}

	public void setSpnSizeZ(JSpinner spnSizeZ) {
		this.spnSizeZ = spnSizeZ;
	}

	public JSpinner getSpnRaster() {
		return spnRaster;
	}

	public void setSpnRaster(JSpinner spnRaster) {
		this.spnRaster = spnRaster;
	}

}
