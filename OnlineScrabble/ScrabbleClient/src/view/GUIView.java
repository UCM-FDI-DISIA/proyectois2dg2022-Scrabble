package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.Controller;

public class GUIView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	private int clientNumPlayer;

	public GUIView(Controller controller, int clientNumPlayer) {
		
		super("SCRABBLE");
		this.controller = controller;
		this.clientNumPlayer = clientNumPlayer;
		initGUI();
	}
	
	private void initGUI() {
		
		this.setSize(new Dimension(1130, 1000));

		JPanel mainPanel = new JPanel(new BorderLayout());
		
		mainPanel.add(new ControlPanel(this.controller, this, clientNumPlayer), BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		mainPanel.add(centerPanel);
		centerPanel.setPreferredSize(new Dimension(1130, 1000));
		
		JPanel boardPanel = new JPanel(new BorderLayout());
		centerPanel.add(boardPanel, BorderLayout.CENTER);
		boardPanel.setPreferredSize(new Dimension(730, 760));
		
		boardPanel.add(new BoardPanel(this.controller, clientNumPlayer), BorderLayout.CENTER);
		boardPanel.add(new InfoPanel(this.controller, this, clientNumPlayer), BorderLayout.NORTH);
		
		JPanel player0 = createPlayerPanel(new Dimension(1130, 100), 0);
		JPanel player1 = createPlayerPanel(new Dimension(200, 960), 1);
		JPanel player2 = createPlayerPanel(new Dimension(1130, 100), 2);
		JPanel player3 = createPlayerPanel(new Dimension(200, 960), 3);
		
		centerPanel.add(player0, BorderLayout.SOUTH);
		centerPanel.add(player1, BorderLayout.EAST);
		centerPanel.add(player2, BorderLayout.NORTH);
		centerPanel.add(player3, BorderLayout.WEST);
		
		setContentPane(mainPanel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}
	
	JPanel createPlayerPanel(Dimension d, int player_number) {
		
		JPanel player = new JPanel();
		
		player.add(new PlayerPanel(this.controller, player_number, clientNumPlayer));
		player.setPreferredSize(d);
		player.setAlignmentY(CENTER_ALIGNMENT);
		player.setAlignmentX(CENTER_ALIGNMENT);
		
		return player;
	}
}
