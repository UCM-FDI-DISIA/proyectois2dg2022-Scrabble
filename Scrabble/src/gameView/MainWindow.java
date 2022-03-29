package gameView;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import scrabble.Controller;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller controller;

	MainWindow(Controller controller) {
		
		super("SCRABBLE");
		
		this.controller = controller;
		
		initGUI();
	}
	
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		BoardPanel boardPanel = new BoardPanel(this.controller);
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		
		setContentPane(mainPanel);
		this.pack();
		this.setVisible(true);
	}
}
