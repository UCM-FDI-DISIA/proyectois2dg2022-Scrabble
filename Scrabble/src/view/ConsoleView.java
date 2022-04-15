package view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import command.Command;
import containers.GamePlayers;
import exceptions.CommandExecuteException;
import exceptions.CommandParseException;
import logic.Game;
import scrabble.Controller;
import simulatedObjects.Box;
import simulatedObjects.SpecialEffects;
import utils.StringUtils;

public class ConsoleView implements ScrabbleObserver {
	
	// Para no estar creando un new Scanner(System.in) cada vez que compruebo.
	private static final Scanner consoleInput = new Scanner(System.in);
	
	private static final String PROMPT = "Comando ([h]elp) > ";
	private static final String CENTRE_SYMBOL = "*";
	private static final String DOUBLE_LETTER_SYMBOL = "•";
	private static final String DOUBLE_WORD_SYMBOL = "░";
	private static final String TRIPLE_LETTER_SYMBOL = "▒";
	private static final String TRIPLE_WORD_SYMBOL = "█";
	private static final String ROW_SEPARATOR_SYMBOL = "-";
	private static final String COLUMN_SEPARATOR_SYMBOL = "|";
	
	private Controller controller;
	
	private Scanner in;
	
	private PrintStream out;
	
	//private boolean humanIsPlaying;
	
	public ConsoleView(Controller controller, InputStream in, OutputStream out) {
		this.controller = controller;
		this.in = new Scanner(in);
		this.out = new PrintStream(out);
		this.controller.addObserver(this);
	}

	public void showStatus(Game game) {
		this.out.print(game.getStatus());
	}

	private void showFirstTurn(String[] lettersObtained, GamePlayers players, int turn) {
		
		StringBuilder buffer = new StringBuilder();
		buffer.append("Elección de turnos:").append(StringUtils.LINE_SEPARATOR);
		
		for(int i = 0; i < players.getNumPlayers(); i++) {
			buffer.append(players.getPlayerName(i)).append(" ha cogido una ")
				  .append(lettersObtained[i]).append(StringUtils.LINE_SEPARATOR);
		}
		
		buffer.append(StringUtils.LINE_SEPARATOR).append("El orden de juego es: ");
		
		for(int i = 0; i < players.getNumPlayers(); i++) {
			buffer.append(players.getPlayerName((i + turn) % players.getNumPlayers()));
			if(i != players.getNumPlayers() - 1) buffer.append(" -> ");
		}
		
		buffer.append(StringUtils.LINE_SEPARATOR);
		
		this.out.println(buffer);
	}

	public void showBoard(Game game) {

		// Numero de digitos del tamaño del tablero.
		int max_indentation_length = (int) Math.log10(game.getBoardSize()) + 1;
		String max_left_indentation = createIndentation(max_indentation_length) + "   ";
		
		// Numero de "ROW_SEPARATOR_SYMBOL", que deben separar filas.
		int row_separator_length = 0;
		
		// Indentación para la primera fila de números.
		this.out.print(max_left_indentation);
		
		// Imprimimos línea de coordenadas del lado superior
		for(int i = 0; i < game.getBoardSize(); i++) {
			
			// Espacio extra que deben tener numeros de ciertas cifras
			int separationBetweenNumbers = max_indentation_length - (i == 0 ? 1 : (int) Math.log10(i) + 1);
			
			if(max_indentation_length % 2 == 0) {
				this.out.print(createIndentation(max_indentation_length / 2) + i + createIndentation(max_indentation_length / 2 + separationBetweenNumbers));
				row_separator_length += max_indentation_length + separationBetweenNumbers + (i == 0 ? 1 : (int) Math.log10(i) + 1);
			}
			
			else {
				this.out.print(createIndentation(max_indentation_length / 2 + 1) + i + createIndentation(max_indentation_length / 2 + separationBetweenNumbers + 2));
				row_separator_length += max_indentation_length + separationBetweenNumbers + 2 + (i == 0 ? 1 : (int) Math.log10(i) + 1);
			}
		}
		
		this.out.print(StringUtils.LINE_SEPARATOR);
		
		// Imprimimos el resto del tablero
		for(int i = 0; i < game.getBoardSize(); i++) {
			
			printRowSeparator(row_separator_length - 1, max_left_indentation);
				
			// Imprimir número de fila
			this.out.print(" ");
			
			int i_indentation_length = max_indentation_length - (i == 0 ? 1 : (int) Math.log10(i) + 1);
			String actual_left_indentation = createIndentation(i_indentation_length);
			
			this.out.print(actual_left_indentation + i + " ");
			
			// Imprimir lo correspondiente a esa fila
			for(int j = 0; j < game.getBoardSize(); j++) {
				Box box = game.getBoxAt(i, j);
				String boxContent = box.toString();
				
				if("".equals(boxContent)) {
					SpecialEffects speEff = box.getSpecialEffect();
					
					if(speEff != null) {
						switch(speEff) {
							case CENTRE: boxContent = CENTRE_SYMBOL; break;
							case DOUBLE_LETTER: boxContent = DOUBLE_LETTER_SYMBOL; break;
							case DOUBLE_WORD: boxContent = DOUBLE_WORD_SYMBOL; break;
							case TRIPLE_LETTER: boxContent = TRIPLE_LETTER_SYMBOL; break;
							case TRIPLE_WORD: boxContent = TRIPLE_WORD_SYMBOL; break;
							default: boxContent = " "; break;
						}
					}
					
					else boxContent = " ";
				}
				
				if(max_indentation_length % 2 == 0)
					this.out.print(COLUMN_SEPARATOR_SYMBOL + createIndentation(max_indentation_length/2) + boxContent + createIndentation(max_indentation_length/2));
				
				else this.out.print(COLUMN_SEPARATOR_SYMBOL + createIndentation(max_indentation_length/2 + 1) + boxContent + createIndentation(max_indentation_length/2 + 1));
			}
				
			this.out.print(COLUMN_SEPARATOR_SYMBOL + StringUtils.LINE_SEPARATOR);
		}
		
		printRowSeparator(row_separator_length - 1, max_left_indentation);
		
		this.out.print(" Doble letra: " + DOUBLE_LETTER_SYMBOL + " || " +
				         "Doble palabra: " + DOUBLE_WORD_SYMBOL + " || " +
				         "Triple letra: " + TRIPLE_LETTER_SYMBOL + " || " + 
				         "Triple palabra: " + TRIPLE_WORD_SYMBOL + 
				         StringUtils.LINE_SEPARATOR);
		
		this.out.print(StringUtils.LINE_SEPARATOR);
	}
	
	private String createIndentation(int length) {
		String indentation = "";
		for(int i = 0; i < length; i++)
			indentation += " ";
		return indentation;
	}
	
	private void printRowSeparator(int row_separator_length, String max_left_indentation) {
		
		this.out.print(max_left_indentation);
		for(int i = 0; i < row_separator_length; i++)
			this.out.print(ROW_SEPARATOR_SYMBOL);
		this.out.print(StringUtils.LINE_SEPARATOR);
	}

	public void showEndMessage(String winners) {
		this.out.print(StringUtils.LINE_SEPARATOR);
		this.out.println(winners);
		this.out.println("¡Gracias por jugar!");
	}

	@Override
	public void onWordWritten(Game game, String word, int posX, int posY, String direction, int points,
			int extraPoints) {
		this.out.println(String.format(
				"El jugador %s escribe la palabra \"%s\" en la posición (%s, %s) en dirección \"%s\".%n",
				game.getPlayers().getPlayerName(game.getCurrentTurn()), word.toUpperCase(), posX, posY, direction.toUpperCase()));
		this.out.print(String.format("¡Gana %s puntos!", points));
		if(extraPoints != 0)
			this.out.println(String.format(" Además, ¡gana %s puntos extra!%n", extraPoints));
		else
			this.out.print(StringUtils.DOUBLE_LINE_SEPARATOR);
			
		pausa();
		
		showBoard(game);
	}

	@Override
	public void onPassed(Game game) {
		this.out.println(String.format("El jugador %s pasa de turno.%n", game.getPlayers().getPlayerName(game.getCurrentTurn())));

		pausa();
		
		showBoard(game);
	}

	@Override
	public void onSwapped(Game game) {
		this.out.println(String.format("El jugador %s intercambia una ficha.%n", game.getPlayers().getPlayerName(game.getCurrentTurn())));
	
		pausa();
		
		showBoard(game);
	}

	@Override
	public void onRegister(Game game) {
		
		Command.gameInitiated(Game.getGameInitiated());
		Command.playersAdded(game.getPlayersAdded());
		
		if (Game.getGameInitiated() && game.getPlayersAdded()) {
			showBoard(game);
		}
		else {
			executeCommand();
		}
		
		controller.update();
	}

	@Override
	public void onReset(Game game) {
		
		this.out.println("Partida iniciada con éxito." + StringUtils.LINE_SEPARATOR);
		
		Command.gameInitiated(Game.getGameInitiated());
		Command.playersAdded(game.getPlayersAdded());
		
		if (Game.getGameInitiated() && game.getPlayersAdded()) {
			showBoard(game);
		}
		else {
			executeCommand();
		}
		
		controller.update();
		
	}

	@Override
	public void onError(String error) {
		this.out.println(error);
		
		controller.update();
	}

	@Override
	public void onUpdate(Game game) {
		
		Command.gameInitiated(Game.getGameInitiated());
		Command.playersAdded(game.getPlayersAdded());
		
		if(Game.getGameInitiated() && game.getPlayersAdded()) {
			if(!game.gameIsFinished()) {
				showStatus(game);
				controller.playTurn();
			}
		}
		else {
			executeCommand();
		}
		
		controller.update();
	}

	@Override
	public void onEnd(String message) {
		showEndMessage(message);
		System.exit(0);
	}
	
	@Override
	public void onFirstTurnDecided(Game game, String[] lettersObtained) {
		showFirstTurn(lettersObtained, game.getPlayers(), game.getCurrentTurn());
		pausa();
		showBoard(game);
	}
	
	@Override
	public void onMovementNeeded() {
		executeCommand();
	}
	
	private void pausa() {
		
		if(Game.isPausePermitted()) {
			this.out.print("Pulsa enter para continuar..." + StringUtils.LINE_SEPARATOR);
			this.in.nextLine();
		}
		
	}

	/*
	private void initGame() throws FileNotFoundException {
		
		this.out.println("¡Bienvenido a Scrabble!" + StringUtils.LINE_SEPARATOR);

		this.out.println("Opciones de inicio:");
		this.out.println("1. Nueva partida.");
		this.out.println("2. Cargar partida de fichero.");
		
		int option = 0;
		
		while(option != 1 && option != 2) {
			
			try {
				this.out.print(StringUtils.LINE_SEPARATOR);
				this.out.print("Selecciona opción: ");
				option = this.in.nextInt();
				
				if(this.in != consoleInput)
					this.out.print(option);
				
				this.out.print(StringUtils.LINE_SEPARATOR);

				
				if(option != 1 && option != 2)
					this.out.println("Opción no válida.");
			}
			
			catch(InputMismatchException ime) {
				this.out.print("[ERROR]" + StringUtils.LINE_SEPARATOR);
				this.in.nextLine();
				this.out.println("¡La entrada debe ser un número!");
			}
		}
		
		this.in.nextLine();
		
		this.out.print(StringUtils.LINE_SEPARATOR);
		
		// Nueva partida
		if(option == 1)
			this.controller.newGame();
		
		else {
			
			File dir = new File("resources/existingGames");
			File[] files = dir.listFiles();
			
			if(files.length != 0) {
				this.out.print("Las partidas disponibles son:" + StringUtils.LINE_SEPARATOR);
				
				for(File file: files) {
					String fileName = file.getName();
					this.out.println("|--> " + fileName);
				}
				
				this.out.print(StringUtils.LINE_SEPARATOR);
				this.out.print("Introduce el nombre de la partida a cargar: ");
				
				String file = this.in.nextLine().trim();
				
				if(this.in != consoleInput)
					this.out.print(file + StringUtils.LINE_SEPARATOR);
				
				file = StringUtils.removeAccents(file);
				
				if(!file.endsWith(".json"))
					file += ".json";
				
				while(!(new File("resources/existingGames/" + file)).exists()) {
					this.out.print(StringUtils.LINE_SEPARATOR);
					this.out.println("No existe una partida con el nombre " + "\"" + file + "\"");
					
					this.out.print("Introduce el nombre de la partida a cargar: ");
					
					file = this.in.nextLine();
					
					if(this.in != consoleInput)
						this.out.print(file + StringUtils.LINE_SEPARATOR);
					
					file = StringUtils.removeAccents(file);
					
					if(!file.endsWith(".json"))
						file += ".json";
				}
				
				this.out.print(StringUtils.LINE_SEPARATOR);
				
				this.controller.loadGame("resources/existingGames/" + file);
			}
			
			else {
				this.out.print(StringUtils.LINE_SEPARATOR);
				this.out.print("No existen partidas anteriores.");
				this.out.print(StringUtils.LINE_SEPARATOR);
			}
		}
	}
	*/
	
	private void executeCommand() {
		
		Command command = askCommand();
		
		if(command != null)
			try {
				command.execute(this.controller, in, out);
			} catch (CommandExecuteException e) {
				this.out.println(e.getMessage());
			}
	}
	
	private Command askCommand() {
		
		Command command = null;
		
		this.out.print(PROMPT);
		String s = this.in.nextLine();
		
		s = StringUtils.removeAccents(s);
		
		if(!this.in.equals(consoleInput))
			this.out.print(s + StringUtils.LINE_SEPARATOR);

		String[] parameters = s.toLowerCase().trim().split(" ");
		
		try {
			command = Command.getCommand(parameters);
		}
		catch(CommandParseException cpe) {
			this.out.println(cpe.getMessage());
		}
		
		return command;		
	}

}