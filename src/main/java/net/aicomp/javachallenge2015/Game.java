package net.aicomp.javachallenge2015;

import java.util.Random;

import net.aicomp.javachallenge2015.entities.Field;

public class Game {
	private static final int FORCED_END_TURN = 100;
	private static final int MAP_WIDTH = 40;
	private static final int INITIAL_TURN = 0;
	private Random rnd;
	private int turn;
	private Player[] players;
	private int[][] board = new int[MAP_WIDTH][MAP_WIDTH];
	private Field field;

	public boolean isInitialState() {
		return turn == INITIAL_TURN;
	}

	public String getInitialInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTurnInformation(int _index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize(String seed) {
		if (seed != null) {
			rnd = new Random(Long.parseLong(seed));
		} else {
			rnd = new Random();
		}
		turn = 0;
		field = new Field();
		players = new Player[Bookmaker.PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player();
		}
		rebirthPhase();
	}

	public boolean isFinished() {
		int livingCnt = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i].life > 0) {
				livingCnt++;
			}
		}
		return livingCnt == 1 || turn > FORCED_END_TURN;
	}

	public void processTurn(String[] commands) {

	}

	public int getTurn() {
		return turn;
	}

	private void printLOG(String command) {
		System.out.println(turn);

		// 残機の出力
		for (Player player : players) {
			System.out.print(player.life + " ");
		}
		System.out.println();

		// ボードを表示
		for (int x = 0; x < MAP_WIDTH; x++) {
			for (int y = 0; y < MAP_WIDTH; y++) {
				System.out.print(" " + board[x][y]);
			}
			System.out.println();
		}

		// いる座標と向きを表示
		for (Player player : players) {
			if (player.isOnBoard()) {
				System.out.println(player.getX() + " " + player.getY() + " "
						+ player.getDir().toString().substring(0, 1));
			} else {
				System.out.println((-1) + " " + (-1) + " "
						+ player.getDir().toString().substring(0, 1));
			}
		}

		// そのターンに行動したプレーヤーの出すコマンドを出力
		System.out.println(command);
	}

	// パネルやプレーヤーを落としたり復活させたりする
	private void rebirthPhase() {
	}

}
