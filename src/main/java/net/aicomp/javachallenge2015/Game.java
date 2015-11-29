package net.aicomp.javachallenge2015;

import java.util.Random;

import net.aicomp.javachallenge2015.log.GameResult;

public class Game {
	private final Random _random;
	private final Player[] _players;
	private final Field _field;
	private final int _maxTurn;
	private final GameResult _result;
	private int _turn;
	private int _currentPlayerIndex;

	public Game(String seed, String maxTurn) {
		if (seed != null) {
			_random = new Random(Long.parseLong(seed));
		} else {
			_random = new Random();
		}
		if (maxTurn != null) {
			_maxTurn = Integer.parseInt(maxTurn);
		} else {
			_maxTurn = 1000;
		}
		_field = new Field();
		_players = new Player[Constants.PLAYERS_NUM];
		for (int i = 0; i < _players.length; i++) {
			_players[i] = new Player(this, _field, _players);
		}
		_result = new GameResult();
	}

	public boolean isFinished() {
		int livingCount = 0;
		for (int i = 0; i < _players.length; i++) {
			if (_players[i].isAlive()) {
				livingCount++;
			}
		}
		return livingCount == 1 || _turn >= _maxTurn;
	}

	public void finish() {
		int livingCount = 0;
		int winnerId = -1;
		for (int i = 0; i < _players.length; i++) {
			if (_players[i].isAlive()) {
				livingCount++;
				winnerId = i;
			}
		}
		if (livingCount != 1) {
			winnerId = -1;
		}
		String log = serializeForAI(new StringBuilder()).toString();
		_result.writeAsJson(log, winnerId);
	}

	public void processTurn(String command) {
		Player turnPlayer = _players[getCurrentPlayerIndex()];
		if (turnPlayer.isAlive()) {
			turnPlayer.setCommand(command);
			turnPlayer.doCommand(_field, _players);
			turnPlayer.refresh();
		}
		_field.refresh(_players);
		_turn++;
		_currentPlayerIndex = _turn % _players.length;
	}

	public int getTurn() {
		return _turn;
	}

	public int getCurrentPlayerIndex() {
		return _currentPlayerIndex;
	}

	public StringBuilder serializeForAI(StringBuilder builder) {
		builder.append(Integer.toString(getCurrentPlayerIndex()));
		builder.append(Constants.LineSeparator);
		builder.append(Integer.toString(_turn));
		builder.append(Constants.LineSeparator);
		_field.serialize(builder);
		for (Player player : _players) {
			player.serialize(builder);
			builder.append(Constants.LineSeparator);
		}
		builder.append(Constants.EOD);
		return builder;
	}

	public StringBuilder serializeForLog(StringBuilder builder) {
		builder.append(Integer.toString(_turn));
		builder.append(Constants.LineSeparator);
		_field.serialize(builder);
		for (Player player : _players) {
			player.serializeOnlyLocation(builder);
			builder.append(Constants.LineSeparator);
		}
		serializePlayerCommands(builder);
		return builder;
	}

	public StringBuilder serializePlayerCommands(StringBuilder builder) {
		String delimiter = "";
		for (int i = 0; i < _players.length; i++) {
			builder.append(delimiter);
			builder.append(_players[i].getCommandValue());
			delimiter = " ";
		}
		return builder;
	}

	public Random getRandom() {
		return _random;
	}

	public GameResult getResult() {
		return _result;
	}
}
