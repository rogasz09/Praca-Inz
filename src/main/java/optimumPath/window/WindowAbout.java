package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.LayoutStyle.ComponentPlacement;

public class WindowAbout extends JDialog {

	private static final long serialVersionUID = 661201541798286059L;
	private final JPanel contentPanel = new JPanel();
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public WindowAbout() {
		setTitle("Informacje o programie");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/icon.png")));
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblName = new JLabel("Optymalna \u015Bcie\u017Cka na mapie rastrowej 3D");
		lblName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
		
		JLabel lblAuthors = new JLabel("Autorzy: Pawe\u0142 Rogaszewski, Tomasz Paw\u0142owski");
		lblAuthors.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		
		JLabel lblPromoter = new JLabel("Promotor: dr in\u017C. Krystyna Rudzi\u0144ska-Korma\u0144ska");
		lblPromoter.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		
		JLabel lblCathedral = new JLabel("<html><center>Katedra System\u00F3w Decyzyjnych i Robotyki<br>\r\nETI Politechnika Gda\u0144ska 2017</center>");
		lblCathedral.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblWersjaProgramu = new JLabel("Wersja programu: 1.0.4.0");
		lblWersjaProgramu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(45, Short.MAX_VALUE)
					.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
					.addGap(29))
				.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
					.addGap(59)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWersjaProgramu)
						.addComponent(lblAuthors)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
							.addComponent(lblCathedral, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblPromoter)))
					.addContainerGap(57, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblWersjaProgramu)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAuthors)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPromoter)
					.addGap(18)
					.addComponent(lblCathedral)
					.addContainerGap(36, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				cancelButton = new JButton("Zamknij");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		createEvents();
	}
	
	private void createEvents() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
