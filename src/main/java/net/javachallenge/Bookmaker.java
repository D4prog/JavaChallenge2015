package net.javachallenge;

import java.util.Scanner;

public class Bookmaker {
	private static final int PLAYERS_NUM = 4;
	private static final int INITIAL_LIFE = 5;
	private static final int MAP_WIDTH = 40;
	private static final int FORCED_END_TURN = (int) 1e9;
	private static final int PANEL_REBIRTH_TURN = 5;

	private static final String READY = "Ready";
	private static final String UP = "U";
	private static final String DOWN = "D";
	private static final String RIGHT = "R";
	private static final String LEFT = "L";
	private static final String ATTACK = "A";

	private static Player[] players;
	private static Scanner scanner;
	private static int turn;
	private static int[][] board = new int[MAP_WIDTH][MAP_WIDTH];

	public static void main(String[] args) {
		scanner = new Scanner(System.in);

		init();
		while (!isFinished()) {
			turn++;
			outputInput();
			panelRebirthPhase();
			actionPhase();
			dropPhase();

		}

	}

	private static void init() {
		turn = 0;
		players = new Player[PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(INITIAL_LIFE);
		}
	}

	private static void outputInput() {

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

class Player {
	public int life;
	public int x, y;
	public boolean onBoard;

	public Player(int life) {
		this.life = life;
		onBoard = true;
	}

	public void drop() {
		life--;
	}
}
