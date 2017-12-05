package optimumPath.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class WindowHelp extends JDialog {

	private static final long serialVersionUID = 2661418001720680006L;
	private final JPanel contentPanel = new JPanel();
	private JButton cancelButton;
	private JEditorPane helpPane;
	private JScrollPane scrollPane;

	/**
	 * Create the dialog.
	 */
	public WindowHelp() {
		setTitle("Pomoc do programu");
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/icon.png"));
		setBounds(100, 100, 633, 493);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			scrollPane = new JScrollPane();
			{
				helpPane = new JEditorPane();
				helpPane.setEditable(false);
				scrollPane.setViewportView(helpPane);
			}
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
					.addGap(0))
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
	
	
	private void loadHelp() {
		try {
			File file = new File("help/help.htm");
			helpPane.setPage(file.toURI().toURL());
		} catch (IOException ex) {
			dispose();
    		JOptionPane.showMessageDialog(null, "Nie uda³o siê wczytaæ pliku help.htm",
    			   								"B³¹d odczytu", JOptionPane.ERROR_MESSAGE);
    	}
	}
	
	private void createEvents() {	
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				loadHelp();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
