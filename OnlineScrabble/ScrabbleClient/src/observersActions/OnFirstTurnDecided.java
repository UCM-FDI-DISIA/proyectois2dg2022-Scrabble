package observersActions;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import containers.GamePlayers;
import containers.GamePlayersBuilder;
import logic.WordsBuilder;
import simulatedObjects.PlayerBuilder;
import simulatedObjects.TileBuilder;
import strategies.EasyStrategyBuilder;
import strategies.HardStrategyBuilder;
import strategies.HumanStrategyBuilder;
import strategies.MediumStrategyBuilder;
import strategies.StrategyBuilder;
import view.ScrabbleObserver;

public class OnFirstTurnDecided extends OnObserverAction {
	
	private List<String> lettersObtained;
	private GamePlayers gamePlayers;
	private int numPlayers;
	private int currentTurn;
	private boolean gameInitiated;
	
	private GamePlayersBuilder gamePlayersBuilder;
	private WordsBuilder wordsBuilder;
	
	OnFirstTurnDecided() {
		
		super("first_turn_decided");

		List<StrategyBuilder> strategyBuilders = new ArrayList<StrategyBuilder>();
		
		strategyBuilders.add(new EasyStrategyBuilder());
		strategyBuilders.add(new MediumStrategyBuilder());
		strategyBuilders.add(new HardStrategyBuilder());
		strategyBuilders.add(new HumanStrategyBuilder());
		
		this.gamePlayersBuilder = new GamePlayersBuilder(new PlayerBuilder(new TileBuilder(), strategyBuilders));
		
		this.wordsBuilder = new WordsBuilder();
	}
	
	@Override
	OnObserverAction interpret(JSONObject jo) {
		
		if(this.type.equals(jo.getString("type"))) {
			
			JSONObject data = jo.getJSONObject("data");
			
			this.lettersObtained = this.wordsBuilder.createWords(data.getJSONObject("letters_obtained"));
			this.gamePlayers = this.gamePlayersBuilder.createGamePlayers(data.getJSONObject("game_players"));
			this.numPlayers = data.getInt("num_players");
			this.currentTurn = data.getInt("current_turn");
			this.gameInitiated = data.getBoolean("game_initiated");
			
			return this;
		}
		
		return null;
	}

	@Override
	public void executeAction(List<ScrabbleObserver> observers) {

		for(ScrabbleObserver o : observers)
			o.onFirstTurnDecided(lettersObtained, gamePlayers, numPlayers, currentTurn, gameInitiated);
	}
	

}
