package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.aicomp.javachallenge2015.log.Logger;

public class Game {
	private static final String EOD = "EOD";
	private final Random random;
	private final Player[] players;
	private final Field field;
	private final int forcedEndTurn;
	private int turn;

	public Game(String seed, String maxTurn) {
		if (seed != null) {
			random = new Random(Long.parseLong(seed));
		} else {
			random = new Random();
		}
		if (maxTurn != null) {
			forcedEndTurn = Integer.parseInt(maxTurn);
		} else {
			forcedEndTurn = 1000;
		}
		turn = 0;
		field = new Field();
		players = new Player[Bookmaker.PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(this, field, players);
		}
	}

	public boolean isFinished() {
		int livingCount = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i].isAlive()) {
				livingCount++;
			}
		}
		return livingCount == 1 || turn >= forcedEndTurn;
	}

	public void finish() {
		int livingCount = 0;
		int winnerId = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i].isAlive()) {
				livingCount++;
				winnerId = i;
			}
		}
		if (livingCount != 1) {
			winnerId = -1;
		}
		String log = getLogInformation(turn % players.length);
		Logger.outputLogObject(log, winnerId);
	}

	public void processTurn(String[] commands) {
		Player turnPlayer = players[turn % players.length];
		if (turnPlayer.isAlive()) {
			String command = null;
			if (commands != null && commands.length > 0) {
				command = commands[0];
			}
			turnPlayer.setCommand(command);
			turnPlayer.doCommand(field, players);
			turnPlayer.refresh();
		}
		field.refresh(players);
		turn++;
	}

	public int getTurn() {
		return turn;
	}

	public String getTurnInformation(int index) {
		List<String> info = new ArrayList<String>();
		info.add(Integer.toString(index));
		info.add(Integer.toString(turn));
		info.addAll(field.getBlockStatus());
		info.addAll(getPlayersStatus());
		info.add(EOD);
		return String.join(System.getProperty("line.separator"), info);
	}

	public String getLogInformation(int index) {
		List<String> info = new ArrayList<String>();
		info.add(Integer.toString(turn));
		info.addAll(field.getBlockStatus());
		info.addAll(getPlayersPlaceAndDirection());
		info.add(getPlayersCommand());
		return String.join(System.getProperty("line.separator"), info);
	}

	public String getMessageInformation(int index) {
		List<String> info = new ArrayList<String>();
		info.add(Integer.toString(turn));
		info.addAll(getPlayersPlaceAndDirection());
		info.add(getPlayersCommand());
		return String.join(System.getProperty("line.separator"), info);
	}

	private String getPlayersCommand() {
		StringBuilder builder = new StringBuilder();
		String delimiter = "";
		for (int i = 0; i < players.length; i++) {
			builder.append(delimiter);
			builder.append(players[i].getCommandValue());
			delimiter = " ";
		}
		return builder.toString();
	}

	private List<String> getPlayersPlaceAndDirection() {
		List<String> ret = new ArrayList<String>();
		for (Player player : players) {
			ret.add(player.getPlaceAndDirection());
		}
		return ret;
	}

	private List<String> getPlayersStatus() {
		List<String> ret = new ArrayList<String>();
		for (Player player : players) {
			ret.add(player.getStatus());
		}
		return ret;
	}

	public Random getRandom() {
		return random;
	}
}
