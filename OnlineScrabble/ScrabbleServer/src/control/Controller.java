package control;

import java.io.FileNotFoundException;

import client.Client;
import containers.GamePlayers;
import view.ScrabbleObserver;

/* APUNTES GENERALES
   
   La clase Controller es la encargada de conectar el modelo y la vista usada.
   
   Sus atributos son:
   - Instancia de Game: para poder conectarse con la lógica del juego.
   - Un String que representa el último fichero usado. Así, se puede controlar
     a qué partida volvemos cuando se invoca el comando reset.
   
 */
public class Controller {
	
	private String lastFileUsed;
	
	private Client client;
	
	public Controller(Client client) {
		this.lastFileUsed = null;
		this.client = client;
	}
	
	/* Método playTurn:
	 * Delega en la clase Game la acción de jugar un turno, ya sea por un jugador humano, o por uno automático
	 */
	public void playTurn() {
		this.client.sendGameAction(ControllerSerializer.serializePlayTurn());
	}
	
	/* Método writeAWord:
	 * Delega en la clase Game la acción de escribir una palabra en el tablero.
	 */
	public void writeAWord(String word, int posX, int posY, String direction) {
		this.client.sendGameAction(ControllerSerializer.serializeWriteAWord(word, posX, posY, direction));
	}
	
	/* Método passTurn:
	 * Delega en la clase Game el paso de turno.
	 */
	public void passTurn() {
		this.client.sendGameAction(ControllerSerializer.serializePassTurn());
	}
	
	/* Método swapTile:
	 * Delega en la clase Game la acción de intercambiar una ficha.
	 * Devuelve un booleano indicando si se ha podido realizar dicho intercambio.
	 */
	public void swapTile() {
		this.client.sendGameAction(ControllerSerializer.serializeSwapTile());
	}
	
	/* Método userExits:
	 * Delega en la clase Game la terminación del juego.
	 */
	public void userExits() {
		this.client.sendGameAction(ControllerSerializer.serializeUserExits());
	}
	
	/* Método reset:
	 * Si el fichero usado más recientemente es nulo, entonces
	 * significa que no se había cargado anteriormente una partida,
	 * así que se debe resetear el juego creando una partida nueva.
	 * En caso contrario, se reseta el juego cargando la partida 
	 * asociada al fichero usado más reciente.
	 */
	public void reset() throws FileNotFoundException {
		
		if(this.lastFileUsed == null)
			newGame();
		
		else loadGame(this.lastFileUsed);
	}

	/* Método update:
	 * Delega en la clase Game la actualización del estado de la partida.
	 */
	public void update() {
		this.client.sendGameAction(ControllerSerializer.serializeUpdate());
	}

	/* Método newGame:
	 * Delega en la clase Game el reseteo de los jugadores.
	 * Delega en la clase GameLoader la creación de una nueva partida 
	 * Establece el último fichero usado a nulo.
	 */
	public void newGame() {
		
		this.lastFileUsed = null;
		this.client.sendGameAction(ControllerSerializer.serializeNewGame());
	}
	
	/* Método loadGame:
	 * Delega en la clase Game el reseteo de los jugadores.
	 * Delega en la clase GameLoader la carga del juego a partir 
	 * del fichero recibido por parámetro, y establece este último
	 * como el fichero usado más reciente.
	 * La excepción lanzada (fichero no encontrado), es recogida en
	 * el método execute de la clase LoadCommand.
	 */
	public void loadGame(String file) {
		
		this.lastFileUsed = file;
		this.client.sendGameAction(ControllerSerializer.serializeLoadGame(file));
	}
	
	/* Método saveGame:
	 * Delega en la clase GameSaver el guardado de partida.
	 * La excepción lanzada (fichero no encontrado), es recogida en
	 * el método execute de la clase SaveCommand.
	 */
	public void saveGame(String file) throws FileNotFoundException {
		this.client.sendGameAction(ControllerSerializer.serializeSaveGame(file));
	}
	
	/* Método addPlayers:
	 * Delega en la clase Game la inicialización de los jugadores del juego.
	 */
	public void addOrChangePlayers(GamePlayers players) {
		this.client.sendGameAction(ControllerSerializer.serializeAddOrChangePlayers(players));
	}

	/* Método addObserver:
	 * Delega en la clase Game la acción de añadir un observador del modelo en la partida.
	 */
	public void addObserver(ScrabbleObserver o) {
		this.client.addObserver(o);
	}
	
	/* Método removeObserver:
	 * Delega en la clase Game la acción de eliminar un observador del modelo en la partida.
	 */
	public void removeObserver(ScrabbleObserver o) {
		this.client.removeObserver(o);
	}
}
