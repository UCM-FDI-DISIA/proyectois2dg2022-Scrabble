package gameView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChooseWordDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Component parent;
	
	private DefaultComboBoxModel<String> directionsModel;
	private JComboBox<String> directionsCombo;
	private String[] directions = { "V", "H" };
	
	private JTextField wordField;
	
	private JLabel position;
	
	private int status;
	
	
	ChooseWordDialog(Component parent) {
		this.parent = parent;
		this.setModal(true);
		initGUI();
	}
	
	private void initGUI() {
		
		status = 0;
	
		setTitle("Write a Word");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.setPreferredSize(new Dimension(400, 60));
		
		JLabel label1 = new JLabel("Choose a word to write in the position ");
		this.position = new JLabel();
		JLabel label2 = new JLabel("and choose a direction.");
		northPanel.add(label1);
		northPanel.add(this.position);
		northPanel.add(label2);
		
		JPanel writingAWordOption = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(writingAWordOption, BorderLayout.CENTER);
		
		JPanel wordOption = new JPanel();
		wordOption.setLayout(new BoxLayout(wordOption, BoxLayout.X_AXIS));
		writingAWordOption.add(wordOption);
		
		wordOption.add(new JLabel("Word: "));
		this.wordField = new JTextField();
		this.wordField.setPreferredSize(new Dimension(60, 20));
		wordOption.add(this.wordField);
		
		JPanel directionsOption = new JPanel();
		directionsOption.setLayout(new BoxLayout(directionsOption, BoxLayout.X_AXIS));
		writingAWordOption.add(directionsOption);
		
		directionsOption.add(new JLabel("Direction: "));
		this.directionsModel = new DefaultComboBoxModel<String>();
		this.directionsCombo = new JComboBox<String>(this.directionsModel);
		directionsOption.add(this.directionsCombo);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		JButton cancel = new JButton();
		cancel.setText("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonsPanel.add(cancel);
		
		JButton ok = new JButton();
		ok.setText("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wordField.getSelectedText() != null && directionsCombo.getSelectedItem() != null) status = 1;
				else  {
					status = 0;
					JOptionPane.showMessageDialog(ChooseWordDialog.this, "Los valores escogidos no son válidos", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				
				setVisible(false);
			}
		});
		buttonsPanel.add(ok);
		
		setContentPane(mainPanel);
		setMinimumSize(new Dimension(350, 200));
		setPreferredSize(new Dimension(400, 205));
		setVisible(false);
	}
	
	public int open(int posX, int posY) {
		
		this.directionsModel.removeAllElements();
		for(String x : this.directions)
			this.directionsModel.addElement(x);
		
		this.position.setText(String.format("(%s, %s)", posX, posY));
		
		setLocationRelativeTo(parent);
		pack();
		setVisible(true);
		
		return status;
	}

	String getSelectedWord() {
		return this.wordField.getSelectedText();
	}

	String getSelectedDirection() {
		return (String) this.directionsCombo.getSelectedItem();
	}
}
