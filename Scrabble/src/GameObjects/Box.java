package GameObjects;

import SpecialEffects.SpecialEffects;

public class Box {

	private Tile tile;
	private SpecialEffects specialEffect;
	private boolean specialEffectAlreadyDone;
	
	Box(SpecialEffects specialEffect) {
		this.specialEffect = specialEffect;
		this.tile = null;
		this.specialEffectAlreadyDone = false;
	}
	
	public void assignTile(Tile tile) {
		
		if (this.tile != null) 
			throw new IllegalArgumentException("Esta casilla ya esta ocupada!");
		
		this.tile = tile;
	}
	
	private int givePoints() {
		
		int points;
		
		if(!this.specialEffectAlreadyDone) {
			this.specialEffectAlreadyDone = true;
			points = this.tile.getPoints() * this.specialEffect.getLetterPointsMultiplier();
		}
		else points = this.tile.getPoints();
		
		return points;
	}

	public Tile getTile() {
		
		return this.tile;
	}	
}
