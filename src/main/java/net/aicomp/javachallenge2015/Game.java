package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
	private int forcedEndTurn = 1000;
	private static final String EOD = "EOD";
	private Random random;
	private int turn;
	private Player[] players;
	private Field field;
	private int winner;

	public void initialize(String seed, String maxTurn) {
		if (seed != null) {
			random = new Random(Long.parseLong(seed));
		} else {
			random = new Random();
		}
		if (maxTurn != null) {
			forcedEndTurn = Integer.parseInt(maxTurn);
		}
		turn = 0;
		winner = -1;
		field = new Field();
		players = new Player[Bookmaker.PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(random);
			players[i].initialize(field.getSpawnablePoints(players));
		}
	}

	public boolean isFinished() {
		int livingCnt = 0;
		int winnerId = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i].isAlive()) {
				livingCnt++;
				winnerId = i;
			}
		}
		if (livingCnt == 1) {
			winner = winnerId;
			return true;
		}
		return turn >= forcedEndTurn;
	}

	public void processTurn(String[] commands) {
		Player turnPlayer = players[turn % players.length];
		if (!turnPlayer.isAlive()) {
			field.refresh(players);
			turn++;
			return;
		}
		String command = null;
		if (commands != null && commands.length > 0) {
			command = commands[0];
		}
		turnPlayer.setCommand(command);
		turnPlayer.doCommand(field, players);
		field.refresh(players);
		turnPlayer.refresh(field.getSpawnablePoints(players));
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
		for (int i = 0; i < players.length; i++) {
			if (i != 0) {
				builder.append(' ');
			}
			builder.append(players[i].getCommandValue());
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

	public int getWinner() {
		return winner;
	}

}
