package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Bookmaker {
	private static boolean DEBUG = false;

	private static final int PLAYERS_NUM = 4;
	private static final int MAP_WIDTH = 40;
	private static final int BLOCK_WIDTH = 5;
	private static final int INITIAL_LIFE = 5;// ゲーム開始時の残機
	private static final int FORCED_END_TURN = 10000;// ゲームが強制終了するターン
	private static final int PANEL_REBIRTH_TURN = 5 * 4;// パネルが再生するまでのターン数
	public static final int PLAYER_REBIRTH_TURN = 5 * 4;// プレイヤーが再生するまでのターン数
	public static final int ATTACKED_PAUSE_TURN = 5 * 4;// 攻撃後の硬直している時間
	public static final int MUTEKI_TURN = 10 * 4;// 再生直後の無敵ターン数
	private static final int REPULSION = 7;// プレイヤーの反発範囲
	public static final int ACTION_TIME_LIMIT = 2000;
	private static final int TIME_TO_FALL = 1 * 4;// 攻撃を受けたマスが落ちるまでの時間（これに距離をかけたものが落ちるまでの時間になる）

	public static final String READY = "READY";
	public static final String EOD = "EOD";
	public static final String UP = "U";
	public static final String DOWN = "D";
	public static final String RIGHT = "R";
	public static final String LEFT = "L";
	public static final String ATTACK = "A";
	public static final String NONE = "N";
	public static final String[] DIRECTION = { UP, LEFT, DOWN, RIGHT };

	private static Player[] players;
	private static Random rnd;
	private static int turn;
	private static int[][] board = new int[MAP_WIDTH][MAP_WIDTH];

	private static final String EXEC_COMMAND = "a";
	private static final String PAUSE_COMMAND = "p";
	private static final String UNPAUSE_COMMAND = "u";
	private static final String SEED_COMMAND = "s";

	public static void main(String[] args) throws InterruptedException,
			ParseException {

		// AIの実行コマンドを引数から読み出す
		Options options = new Options()
				.addOption(
						EXEC_COMMAND,
						true,
						"The command and arguments with double quotation marks to execute AI program (e.g. -a \"java MyAI\")")
				.addOption(
						PAUSE_COMMAND,
						true,
						"The command and arguments with double quotation marks to pause AI program (e.g. -p \"echo pause\")")
				.addOption(
						UNPAUSE_COMMAND,
						true,
						"The command and arguments with double quotation marks to unpause AI program (e.g. -u \"echo unpause\")")
				.addOption(SEED_COMMAND, true, "The seed of the game");

		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(options, args);

		if (!hasCompleteArgs(line)) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("java -jar Bookmaker.jar [OPTIONS]\n"
					+ "[OPTIONS]: ", "", options, "", true);
			return;
		}

		String[] execAICommands = line.getOptionValues(EXEC_COMMAND);
		String[] pauseAICommands = line.hasOption(PAUSE_COMMAND) ? line
				.getOptionValues(PAUSE_COMMAND) : new String[PLAYERS_NUM];
		String[] unpauseAICommands = line.hasOption(UNPAUSE_COMMAND) ? line
				.getOptionValues(UNPAUSE_COMMAND) : new String[PLAYERS_NUM];

		// 乱数・ターン数の初期化
		if (line.hasOption(SEED_COMMAND)) {
			rnd = new Random(Long.parseLong(line.getOptionValue(SEED_COMMAND)));
		} else {
			rnd = new Random();
		}
		turn = 0;

		// AIの実行
		players = new Player[PLAYERS_NUM];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(INITIAL_LIFE, execAICommands[i],
					pauseAICommands[i], unpauseAICommands[i]);
		}

		// プレイヤーを初期配置する
		rebirthPhase();

		// ゲーム
		while (!isFinished()) {
			int turnPlayer = turn % PLAYERS_NUM;

			// AIに情報を渡してコマンドを受け取る
			String command = infromationPhase(turnPlayer);

			// 盤面の状態とAIの出したコマンドをログに出力
			printLOG(command);

			// DEBUGプレイ
			if (DEBUG && turnPlayer == 0 && players[turnPlayer].isOnBoard()
					&& !players[turnPlayer].isPausing(turn)) {
				command = new Scanner(System.in).next();
			}

			// コマンドを実行する
			actionPhase(turnPlayer, command);

			// パネル・プレイヤーの落下と復活
			rebirthPhase();

			turn++;
		}

		killAllPlayer();
		System.out.println("Game Finished!");
	}

	private static void killAllPlayer() {
		for (Player player : players) {
			if (player.isAlive()) {
				player.killPlayer();
			}
		}
	}

	/**
	 * コマンドライン引数の確認。与えられたコマンドライン引数がnullでなく、実行コマンドは必ず4つ、
	 * ポーズコマンドとアンポーズコマンドは1つも無いか4つ与えられているかを確認する。
	 * 
	 * @param line
	 * @author J.Kobayashi
	 * @return 条件が満たされるならば {@code true}、それ以外の場合は{@code false}
	 */
	private static boolean hasCompleteArgs(CommandLine line) {
		if (line == null) {
			return false;
		}
		if (!line.hasOption(EXEC_COMMAND)
				|| line.getOptionValues(EXEC_COMMAND).length != PLAYERS_NUM) {
			return false;
		}
		if (!line.hasOption(PAUSE_COMMAND)) {
			return true;
		}
		if (line.getOptionValues(PAUSE_COMMAND).length != PLAYERS_NUM) {
			return false;
		}
		if (!line.hasOption(UNPAUSE_COMMAND)) {
			return true;
		}
		if (line.getOptionValues(UNPAUSE_COMMAND).length != PLAYERS_NUM) {
			return false;
		}
		return true;
	}

	private static void printLOG(String command) {
		// ターン数の出力
		System.out.println(turn);

		// 残機の出力
		for (Player player : players) {
			System.out.print(player.life + " ");
		}
		System.out.println();

		// ボードを表示
		for (int x = 0; x < MAP_WIDTH; x++) {
			outer: for (int y = 0; y < MAP_WIDTH; y++) {
				if (DEBUG) {
					// プレイヤーがいるならそれを表示
					for (int playerID = 0; playerID < PLAYERS_NUM; playerID++) {
						Player player = players[playerID];
						if (player.isOnBoard() && player.x == x
								&& player.y == y) {
							char c = (char) (playerID + 'A');
							System.out.print(c + "" + c);
							continue outer;
						}
					}
				}

				System.out.print(" " + board[x][y]);
			}
			System.out.println();
		}

		// いる座標と向きを表示
		for (Player player : players) {
			if (player.isOnBoard()) {
				System.out.println(player.x + " " + player.y + " "
						+ Bookmaker.DIRECTION[player.dir]);
			} else {
				System.out.println((-1) + " " + (-1) + " "
						+ Bookmaker.DIRECTION[player.dir]);
			}
		}

		// そのターンに行動したプレーヤーの出すコマンドを出力
		System.out.println(command);
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
				search: while (true) {
					int x = nextInt();
					int y = nextInt();
					for (int j = 0; j < PLAYERS_NUM; j++) {
						if (i == j) {
							continue;
						}

						Player other = players[j];
						if (other.isOnBoard()
								&& dist(x, y, other.x, other.y) <= REPULSION) {
							// 敵に近過ぎたらだめ
							continue search;
						}
					}

					// x,yに復活させる
					p.reBirthOn(x, y, turn);
					p.dir = nextDir();
					break;
				}
			}
		}
	}

	// AIに情報を渡してコマンドを受け取る
	private static String infromationPhase(int turnPlayer) {
		if (!players[turnPlayer].isAlive()) {
			return NONE;
		}

		// AIに渡すための情報を整形
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

		// 情報をAIに渡してコマンドを受け取る
		String command = players[turnPlayer].getAction(turnPlayer, turn, board,
				lifes, wheres);

		return command;
	}

	// AIから受け取ったアクションを実行する
	private static void actionPhase(int turnPlayer, String command) {
		Player p = players[turnPlayer];

		if (!p.isOnBoard() || p.isPausing(turn) || command == null
				|| command.equals(NONE)) {
			return;
		}

		// 攻撃を処理
		if (command.equals(ATTACK)) {
			// 今いるブロックを出す
			int xNow = p.x / BLOCK_WIDTH;
			int yNow = p.y / BLOCK_WIDTH;
			for (int x = 0; x < MAP_WIDTH; x++) {
				for (int y = 0; y < MAP_WIDTH; y++) {
					int xBlock = x / BLOCK_WIDTH;
					int yBlock = y / BLOCK_WIDTH;
					if (p.dir == 0) {
						// 上向きの時
						// xが減っていく
						// yは同じ
						if (yBlock == yNow && xBlock < xNow && board[x][y] == 0) {
							board[x][y] = dist(xBlock, yBlock, xNow, yNow)
									* TIME_TO_FALL;
						}
					} else if (p.dir == 1) {
						// 右向きの時
						// yは増えていき、xは同じ
						if (xBlock == xNow && yBlock < yNow && board[x][y] == 0) {
							board[x][y] = dist(xBlock, yBlock, xNow, yNow)
									* TIME_TO_FALL;
						}
					} else if (p.dir == 2) {
						// 下向きの時
						// xは増え、yは同じ
						if (yBlock == yNow && xBlock > xNow && board[x][y] == 0) {
							board[x][y] = dist(xBlock, yBlock, xNow, yNow)
									* TIME_TO_FALL;
						}
					} else if (p.dir == 3) {
						// 左向きの時
						if (xBlock == xNow && yBlock > yNow && board[x][y] == 0) {
							board[x][y] = dist(xBlock, yBlock, xNow, yNow)
									* TIME_TO_FALL;
						}
					}
				}
			}

			// 攻撃すると硬直する
			p.attackedPause(turn);
			return;
		}

		// 移動処理
		{
			int tox = -1, toy = -1;
			if (command.equals(UP)) {
				tox = p.x - 1;
				toy = p.y;
			} else if (command.equals(RIGHT)) {
				tox = p.x;
				toy = p.y + 1;
			} else if (command.equals(DOWN)) {
				tox = p.x + 1;
				toy = p.y;
			} else if (command.equals(LEFT)) {
				tox = p.x;
				toy = p.y - 1;
			} else {
				return;
			}

			// ボード外への移動を指定している場合はスルー
			if (!isInside(tox, toy)) {
				return;
			}

			p.directTo(command);
			// 移動先でぶつからないかどうかチェック
			for (int i = 0; i < PLAYERS_NUM; i++) {
				if (!players[i].isOnBoard() || i == turnPlayer) {
					continue;
				}

				if (dist(tox, toy, players[i].x, players[i].y) < REPULSION) {
					// 移動先でぶつかる時
					return;
				}
			}

			// ぶつからなければ移動
			p.moveTo(tox, toy);
		}
	}

	// マンハッタン距離計算
	private static int dist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	// ランダムな座標を返す
	private static int nextInt() {
		int ret = (int) (rnd.nextDouble() * MAP_WIDTH);
		return ret;
	}

	// ランダムな向きを返す
	private static int nextDir() {
		int rng = rnd.nextInt(4);
		return rng;
	}

	private static boolean isInside(int x, int y) {
		return 0 <= x && x < MAP_WIDTH && 0 <= y && y < MAP_WIDTH;
	}

	private static boolean isFinished() {
		int livingCnt = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i].life > 0) {
				livingCnt++;
			}
		}
		return livingCnt == 1 || turn > FORCED_END_TURN;
	}
}