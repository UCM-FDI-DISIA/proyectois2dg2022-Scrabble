package command;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import containers.GamePlayers;
import scrabble.Controller;
import storage.GameLoader;
import utils.StringUtils;
import view.ConsoleView;

public class AddPlayersCommand extends Command {
	
	private static final String NAME = "jugadores";

	private static final String DETAILS = "[j]ugadores";

	private static final String SHORTCUT = "j";

	private static final String HELP = "añadir los jugadores del juego";
	
	public AddPlayersCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	/* Sobrescritura del método execute:
	 * Provoca la terminación del juego.
	 */

	@Override
	public boolean execute(Controller controller, Scanner in, PrintStream out) {
		
		controller.addPlayers(createPlayers(in, out));
		
		return false;
	}
	
	private GamePlayers createPlayers(Scanner in, PrintStream out) {
		
		int numPlayers = selectNumPlayers(in, out);
		
		JSONArray players = new JSONArray();
		
		while(players.length() < numPlayers) {
			
			out.print(StringUtils.LINE_SEPARATOR);
			out.print("Estrategia del jugador " + (players.length() + 1) + " [humana, fácil, media, difícil]: ");
			
			String notTreatedStrategy = in.nextLine().trim();
			
			notTreatedStrategy = StringUtils.removeAccents(notTreatedStrategy);
			String strategy = takeType(notTreatedStrategy);
			
			if(!ConsoleView.isInputFromConsole)
				out.print(notTreatedStrategy);
			
			if(strategy != null) {
				
				JSONObject strategyJO = new JSONObject();
				strategyJO.put("strategy_type", strategy.toString());
				
				JSONObject player = new JSONObject();
				player.put("strategy", strategyJO);
				player.put("total_points", 0);
				
				if(strategy.equalsIgnoreCase("human_strategy")) {
					out.print("Nombre del jugador humano " + (players.length() + 1) + ": ");
					String name = in.nextLine().trim();
					
					if(!ConsoleView.isInputFromConsole)
						out.print(name + StringUtils.LINE_SEPARATOR);
					
					if(checkPlayerNames(name, players)) {
						player.put("name", name);
						players.put(player);
					}
					else out.print("Ya hay un jugador con el nombre " + name + StringUtils.LINE_SEPARATOR);
				}
				else players.put(player);
			}
			else {
				out.print("La estrategia introducida no es válida.");
				out.print(StringUtils.LINE_SEPARATOR);
			}
		}
		
		out.print(StringUtils.LINE_SEPARATOR);
		
		JSONObject data = new JSONObject();
		data.put("players", players);
	
		return GameLoader.createPlayers(data);
	}
		
	private int selectNumPlayers(Scanner in, PrintStream out) {
			
		int numPlayers = 0;
		boolean done = false;
		out.print(StringUtils.LINE_SEPARATOR);
		out.print("Selecciona el número de jugadores (2-4): ");
		
		while (!done) {
			try {
				numPlayers = in.nextInt();
				
				if(!ConsoleView.isInputFromConsole)
					out.print(numPlayers);
				
				if (numPlayers < 2 || numPlayers > 4) {
					out.print("El número de jugadores debe estar entre 2 y 4.");
					out.print(StringUtils.DOUBLE_LINE_SEPARATOR);
					out.print("Selecciona el número de jugadores (2-4): ");
				}
				
				else done = true;
				
			}
			catch (InputMismatchException ime) {
				
				if(!ConsoleView.isInputFromConsole)
					out.print("[ERROR]" + StringUtils.LINE_SEPARATOR);
				
				out.print("¡La entrada debe ser un número!");
				out.print(StringUtils.DOUBLE_LINE_SEPARATOR);
				out.print("Selecciona el número de jugadores (2-4): ");
				in.nextLine();
			}
		}
		
		// Para que la entrada sea correcta.
		in.nextLine();  
		
		return numPlayers;
	}

	private static boolean checkPlayerNames(String name, JSONArray players) {
		
		int i = 0;
		while(i < players.length()) {
			if(players.getJSONObject(i).has("name") && players.getJSONObject(i).getString("name").equalsIgnoreCase(name))
				return false;
			++i;
		}
		
		return true;
	}
	
	private static String takeType(String type) {

		type = StringUtils.removeAccents(type);
		type = type.toLowerCase();
		
		switch(type) {
		case "facil":
			return "easy_strategy";
		case "media":
			return "medium_strategy";
		case "dificil":
			return "hard_strategy";
		case "humana":
			return "human_strategy";
		default:
			return null;
		}
	}
}
