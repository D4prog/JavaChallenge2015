package net.javachallenge;

import java.util.ArrayList;

public class Bookmaker {
	private static final int PLAYERS_NUM = 4;
	private static final int INITIAL_LIFE = 5;
	private static final int MAP_WIDTH = 40;
	private static final int FORCED_END_TURN = (int) 1e9;
	private static final int PANEL_REBIRTH_TURN = 5;

	public static final String READY = "Ready";
	private static final String UP = "U";
	private static final String DOWN = "D";
	private static final String RIGHT = "R";
	private static final String LEFT = "L";
	private static final String ATTACK = "A";

	private static Player[] players;
	private static int turn;
	private static int[][] board = new int[MAP_WIDTH][MAP_WIDTH];

	public static void main(String[] args) {

		init();
		while (!isFinished()) {
			turn++;
			panelRebirthPhase();

			ArrayList<Integer> lifeList = new ArrayList<Integer>();
			ArrayList<String> whereList = new ArrayList<String>();

			for (int i = 0; i < PLAYERS_NUM; i++) {
				lifeList.add(players[i].life);
				whereList.add(players[i].x + " " + players[i].y);
			}
			for (int i = 0; i < PLAYERS_NUM; i++) {
				players[i].putInformation(i, turn, board, lifeList, whereList);
			}

			ArrayList<String> commandList = new ArrayList<String>();
			for (int i = 0; i < PLAYERS_NUM; i++) {
				commandList.add(players[i].getAction());
			}
			for (String string : commandList) {
				System.out.print(string + " ");
			}
			System.out.println();

			actionPhase();
			dropPhase();
		}

	}

	private static void init() {
		turn = 5000;
		players = new Player[PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(INITIAL_LIFE,
					"/home/nkenkou/Desktop/tekitoAI");// TODO
			players[i].x = 0;
			players[i].y = 0;
		}
	}

	private static void panelRebirthPhase() {

	}

	private static void actionPhase() {

	}

	private static void dropPhase() {

	}

	private static boolean isFinished() {
		int livingCnt = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i].life > 0) {
				livingCnt++;
			}
		}
		return livingCnt == 0;
	}

}
