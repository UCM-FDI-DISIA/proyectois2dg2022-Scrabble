package gameObjects;

public enum SpecialEffects {

	DOUBLE_LETTER, TRIPLE_LETTER, DOUBLE_WORD, TRIPLE_WORD, CENTRE;
	
	public int getLetterPointsMultiplier() {
		
		if (DOUBLE_LETTER.equals(this)) return 2;
		
		if(TRIPLE_LETTER.equals(this)) return 3;
		
		return 1;
	}
	
	public static SpecialEffects stringToSpecialEffect(String specialEffect) {
		
		if (specialEffect.equals(DOUBLE_LETTER.name())) return DOUBLE_LETTER;
		
		if (specialEffect.equals(TRIPLE_LETTER.name())) return TRIPLE_LETTER;
		
		if (specialEffect.equals(DOUBLE_WORD.name())) return DOUBLE_WORD;
		
		if (specialEffect.equals(TRIPLE_WORD.name())) return TRIPLE_WORD;
		
		if (specialEffect.equals(CENTRE.name())) return CENTRE;
		
		return null;
	}
	
	public int getWordPointsMultiplier() {
		
		if (DOUBLE_WORD.equals(this)) return 2;
		
		if(TRIPLE_WORD.equals(this)) return 3;
		
		return 1;
	}
}