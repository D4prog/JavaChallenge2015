package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
	private int forcedEndTurn = 10000;
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
		turnPlayer.setCommand(commands[0]);
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
		info.add(getPlayersLife());
		info.addAll(field.getStatus());
		info.addAll(getPlayersPlace());
		info.add(EOD);
		return String.join(System.getProperty("line.separator"), info);
	}

	public String getLogInformation(int index) {
		List<String> info = new ArrayList<String>();
		info.add(Integer.toString(turn));
		info.add(getPlayersLife());
		info.addAll(field.getStatus());
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

	private List<String> getPlayersPlace() {
		List<String> ret = new ArrayList<String>();
		for (Player player : players) {
			ret.add(player.getPlace());
		}
		return ret;
	}

	private String getPlayersLife() {
		String ret = "";
		for (int i = 0; i < players.length; i++) {
			if (i != 0) {
				ret += " ";
			}
			ret += players[i].getLife();
		}
		return ret;
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

	public int getWinner() {
		return winner;
	}

}
