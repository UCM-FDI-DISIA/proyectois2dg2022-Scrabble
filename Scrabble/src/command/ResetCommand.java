package command;

import java.io.FileNotFoundException;

import exceptions.CommandExecuteException;
import scrabble.Controller;

public class ResetCommand extends Command {

	private static final String NAME = "reset";

	private static final String DETAILS = "[r]eset";

	private static final String SHORTCUT = "r";

	private static final String HELP = "resetear la partida";
	
	public ResetCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}
	
	@Override
	public boolean execute(Controller controller) throws CommandExecuteException {
		
		try {
			controller.reset();
		}
		catch(FileNotFoundException fnfe) {
			throw new CommandExecuteException("El fichero de reseteo no se ha podido encontrar.", fnfe);
		}
		
		return false;
	}
}
