package Scrabble;

import GameUtils.StringUtils;

public class Main {

	public static void main(String[] args) {

		System.out.println("Bienvenido a Scrabble!" + StringUtils.LINE_SEPARATOR);
		Controller controller = new Controller();
		controller.run();
		
	}
	
	

}
