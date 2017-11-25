package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import optimumPath.common.Point3d;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WindowNewMap extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8024013578434360788L;
	private final JPanel contentPanel = new JPanel();
	private JSpinner spnSizeX;
	private JSpinner spnSizeY;
	private JSpinner spnSizeZ;
	
	private JButton okButton;
	private JButton cancelButton;

	private boolean isOk = false;
	//private JDialog newMap;
	/**
	 * Create the dialog.
	 */
	public WindowNewMap() {
		//newMap = this;
		
		setResizable(false);
		setTitle("Nowa mapa");
		setBounds(100, 100, 280, 218);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblRozmiarX = new JLabel("Rozmiar X:");
		JLabel lblRozmiarY = new JLabel("Rozmiar Y:");
		JLabel lblRozmiarZ = new JLabel("Rozmiar Z:");
		
		JLabel lblUstawieniaRozmiaruMapy = new JLabel("Ustawienia rozmiaru mapy");
		
		spnSizeX = new JSpinner();
		spnSizeY = new JSpinner();
		spnSizeZ = new JSpinner();
		
		spnSizeX.setModel(new SpinnerNumberModel(10, 3, 100, 1));
		spnSizeY.setModel(new SpinnerNumberModel(10, 3, 100, 1));
		spnSizeZ.setModel(new SpinnerNumberModel(10, 1, 100, 1));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUstawieniaRozmiaruMapy)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblRozmiarY)
								.addComponent(lblRozmiarX)
								.addComponent(lblRozmiarZ))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(spnSizeX, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
								.addComponent(spnSizeY)
								.addComponent(spnSizeZ))))
					.addGap(38))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblUstawieniaRozmiaruMapy)
					.addGap(10)
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
					.addGap(40))
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
	
}
