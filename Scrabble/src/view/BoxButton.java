package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import logic.Game;
import scrabble.Controller;
import simulatedObjects.Box;

public class BoxButton extends JButton implements ScrabbleObserver {
	
	private static final long serialVersionUID = 1L;
	
	private int posX;
	private int posY;
	private Box box;
	private ChooseWordDialog chooseWordDialog;
	
	private Controller controller;
	
	private boolean enableButton;

	BoxButton(Controller controller, int x, int y, ChooseWordDialog chooseWordDialog) {
		
		this.posX = x;
		this.posY = y;
		this.box = null;
		this.chooseWordDialog = chooseWordDialog;
		this.controller = controller;
		
		initGUI();
		this.controller.addObserver(this);
	}
	
	private void initGUI() {
		
		enableButton = false;
		
		setToolTipText(String.format("Casilla (%s, %s)", this.posX, this.posY));
		setIcon(new ImageIcon("resources/icons/letters/box_default_icon.png"));
		setPreferredSize(new Dimension(49, 49));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.box = new Box(null, null, false);
		
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(box != null && box.getTile() == null && enableButton) {
					int status = chooseWordDialog.open(posX, posY);
					if(status == 1) {
						String word = chooseWordDialog.getSelectedWord();
						String direction = chooseWordDialog.getSelectedDirection();
						controller.writeAWord(word, posX, posY, direction);
					}
				}
			}
		});	
	}

	@Override
	public void onWordWritten(Game game, String word, int posX, int posY, String direction, int points, int extraPoints) {
		setImage();
	}

	@Override
	public void onRegister(Game game) {
		this.box = game.getBoxAt(this.posX, this.posY);
		if(box != null) setImage();
		enableButton = false;
	}

	@Override
	public void onReset(Game game) {
		this.box = game.getBoxAt(this.posX, this.posY);
		setImage();
		enableButton = false;
	}
	
	private void setImage() {
		
		if(box.getTile() != null) {
			this.setIcon(new ImageIcon("resources/icons/letters/" + box.getTile().getLetter().toUpperCase() + ".png"));
		}
		else if(box.getSpecialEffect() != null)
			this.setIcon(new ImageIcon("resources/icons/special_effects/" + box.getSpecialEffect() + ".png"));
	}

	@Override
	public void onPassed(Game game) {}

	@Override
	public void onSwapped(Game game) {}

	@Override
	public void onError(String error) {}

	@Override
	public void onUpdate(Game game) {
		enableButton = false;
	}

	@Override
	public void onEnd(String message) {}

	@Override
	public void onFirstTurnDecided(Game game, String[] lettersObtained) {}

	@Override
	public void onMovementNeeded() {
		enableButton = true;
	}
}
