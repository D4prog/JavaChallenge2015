package net.javachallenge;

import java.util.ArrayList;
import java.util.Random;

public class Bookmaker {
	private static final boolean DEBUG = true;

	private static final int PLAYERS_NUM = 4;
	private static final int INITIAL_LIFE = 5;
	private static final int MAP_WIDTH = 40;
	private static final int FORCED_END_TURN = 10000;
	private static final int PANEL_REBIRTH_TURN = 5;
	public static final int PLAYER_REBIRTH_TURN = 5;
	public static final int MUTEKI_TURN = 10;
	private static final int REPULSION = 3;// プレイヤーの反発範囲

	public static final String READY = "Ready";
	private static final String UP = "U";
	private static final String DOWN = "D";
	private static final String RIGHT = "R";
	private static final String LEFT = "L";
	private static final String ATTACK = "A";
	private static final String NONE = "N";

	private static Player[] players;
	private static Random rnd;
	private static int turn;
	private static int[][] board = new int[MAP_WIDTH][MAP_WIDTH];

	public static void main(String[] args) {
		if (DEBUG) {
			args = new String[]{"/home/nkenkou/Desktop/tekitoAI",
					"/home/nkenkou/Desktop/tekitoAI",
					"/home/nkenkou/Desktop/tekitoAI",
					"/home/nkenkou/Desktop/tekitoAI"};
		}

		rnd = new Random(System.currentTimeMillis());
		turn = 0;
		players = new Player[PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(INITIAL_LIFE, args[i]);
			// TODO
		}

		while (!isFinished()) {
			turn++;
			rebirthPhase();
			infromationPhase();
			actionPhase();
		}

	}

	// パネルやプレーヤーを落としたり復活させたりする
	private static void rebirthPhase() {
		// パネルを落としたり復活させたりする
		for (int i = 0; i < MAP_WIDTH; i++) {
			for (int j = 0; j < MAP_WIDTH; j++) {
				if (board[i][j] < 0) {
					board[i][j]++;
				} else if (board[i][j] == 1) {
					board[i][j] = -PANEL_REBIRTH_TURN;
				} else if (board[i][j] > 1) {
					board[i][j]--;
				}
			}
		}

		// プレイヤーを落としたり復活させたりする
		for (int i = 0; i < PLAYERS_NUM; i++) {
			Player p = players[i];
			// 落とす
			if (p.isOnBoard() && !p.isMuteki(turn)) {
				if (board[p.x][p.y] < 0) {
					p.drop(turn);
				}
			} else if (p.isAlive() && !p.isOnBoard() && p.rebirthTurn == turn) {
				// 復活させる

				// 復活場所を探す
				search : while (true) {
					int x = nextInt();
					int y = nextInt();
					for (Player other : players) {
						if (other.isOnBoard()
								&& dist(x, y, other.x, other.y) <= REPULSION) {
							// 敵に近過ぎたらだめ
							continue search;
						}
					}

					p.reBirthOn(x, y, turn);
					break;
				}
			}
		}
	}

	// AIに情報を渡してコマンドを受け取る
	private static ArrayList<String> infromationPhase() {
		ArrayList<Integer> lifes = new ArrayList<Integer>();
		ArrayList<String> wheres = new ArrayList<String>();

		for (int i = 0; i < PLAYERS_NUM; i++) {
			lifes.add(players[i].life);
			if (players[i].isOnBoard()) {
				wheres.add(players[i].x + " " + players[i].y);
			} else {
				wheres.add((-1) + " " + (-1));
			}
		}

		// 情報をAIに渡す
		for (int id = 0; id < PLAYERS_NUM; id++) {
			if (!players[id].isAlive()) {
				continue;
			}
			players[id].putInformation(id, turn, board, lifes, wheres);
		}

		// コマンドを受け取る
		ArrayList<String> commandList = new ArrayList<String>();
		for (Player p : players) {
			if (!p.isAlive()) {
				commandList.add(NONE);
			}
			commandList.add(p.getAction());
		}
		return commandList;
	}

	// AIから受け取ったアクションを実行する
	private static void actionPhase() {
	}

	// マンハッタン距離計算
	private static int dist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	private static int nextInt() {
		// ランダムな座標を返す
		int ret = (int) (rnd.nextDouble() * MAP_WIDTH);
		return ret;
	}

	private static boolean isFinished() {
		int livingCnt = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i].life > 0) {
				livingCnt++;
			}
		}
		return livingCnt == 0 || turn < FORCED_END_TURN;
	}
}
