package net.aicomp.javachallenge2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.aicomp.javachallenge2015.entities.Field;
import net.aicomp.javachallenge2015.entities.Point;
import net.exkazuu.gameaiarena.api.Direction4;

public class Player {
	public static final int ACTION_TIME_LIMIT = 2000;

	private static final String NONE = "N";
	private String execCommand = null;
	private String pauseCommand = null;
	private String unpauseCommand = null;
	private Process aiProcess;
	private BufferedReader reader;
	private PrintWriter writer;

	public int life;
	private Point point;
	public int rebirthTurn = 0;
	private int mutekiTurn = -1;
	private int pauseTurn = -1;
	private Direction4 dir;

	private ICommand command;

	public Player(int life, String exec, String pause, String unpause) {
		this.life = life;
		this.execCommand = exec;
		this.pauseCommand = pause;
		this.unpauseCommand = unpause;
		if (exec.length() == 0 || !runPlayer()) {
			life = 0;
		}
	}

	// AIを開始する
	private boolean runPlayer() {
		aiProcess = null;
		try {
			aiProcess = Runtime.getRuntime().exec(execCommand);
		} catch (Exception e) {
			System.err.println("ERROR: Unable to execute the command: \""
					+ execCommand + "\".");
			aiProcess = null;
			return false;
		}

		reader = new BufferedReader(new InputStreamReader(
				aiProcess.getInputStream()));
		writer = new PrintWriter(aiProcess.getOutputStream());
		new ErrorStreamRedirector(aiProcess.getErrorStream()).start();
		try {
			if (reader.readLine().equals(Bookmaker.READY)) {
				return true;
			}
		} catch (IOException e) {
			return false;
		}
		return commandExec(pauseCommand);
	}

	/**
	 * AIからコマンドを受信し、格納する。
	 */
	public void getCommand() {
		ArrayList<String> commands = new ArrayList<String>();
		GetActionThread thread = new GetActionThread(reader, commands);
		thread.start();
		try {
			thread.join(ACTION_TIME_LIMIT);
			if (commands.isEmpty()) {
				killPlayer();
				commands.add(NONE);
			}
		} catch (InterruptedException e1) {
			killPlayer();
			commands.add(NONE);
		}

		// pause
		if (!commandExec(pauseCommand)) {
			command = CommandBuilder.createCommand(NONE);
			return;
		}

		command = CommandBuilder.createCommand(commands.get(0));
	}

	/**
	 * AIを終了する。
	 */
	public void killPlayer() {
		life = 0;
		if (aiProcess != null) {
			try {
				aiProcess.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean commandExec(String command) {
		Process process = null;

		if (command != null) {
			try {
				process = Runtime.getRuntime().exec(command);
				int c = process.waitFor();
				System.out.println(command + c);
			} catch (Exception e) {
				System.err.println("ERROR: Unable to execute the command: "
						+ command + ".");
				return false;
			}
		}
		return true;
	}

	// 落とす
	public void drop(int turn) {
		life--;
		rebirthTurn = Bookmaker.PLAYER_REBIRTH_TURN + turn;
		if (life == 0) {
			killPlayer();
		}
	}

	// 生きているか（プレイ続行可能か）
	public boolean isAlive() {
		return life > 0;
	}

	// 生きてボードの上に乗っているか
	public boolean isOnBoard() {
		return isAlive() && point.isPositive();
	}

	// 硬直中か
	public boolean isPausing(int turn) {
		return pauseTurn >= turn;
	}

	// 攻撃後の硬直
	public void attackedPause(int turn) {
		pauseTurn = turn + Bookmaker.ATTACKED_PAUSE_TURN;
	}

	// 無敵状態か
	public boolean isMuteki(int turn) {
		return mutekiTurn >= turn;
	}

	// (x,y)に復活させる
	public void reBirthOn(int x, int y, int turn) {
		moveTo(x, y);
		if (turn > 0) {
			// turn=0の時は初期配置なので無敵にならない
			mutekiTurn = turn + Bookmaker.MUTEKI_TURN;
		}
	}

	// (x,y)に移動させる
	public void moveTo(int x, int y) {
		point.moveTo(x, y);
	}

	/**
	 * プレイヤーの向きを決める。
	 * 
	 * @param direction
	 *            決める向き
	 */
	public void directTo(Direction4 direction) {
		dir = direction;
	}

	public void printPlayerInfo() {
		System.out.println(point.toString() + " " + dir.name().substring(0, 1));
	}

	/**
	 * 
	 */
	public void setRandomDir() {
		dir = Direction4.getRandomDir();
	}

	/**
	 * プレイヤーに情報を送る。
	 * 
	 * @param playerId
	 * @param turn
	 * @param field
	 * @param players
	 */
	public void sendInformation(int playerId, int turn, Field field,
			Player[] players) {
		writer.println(playerId);
		writer.println(turn);

		for (int i = 0; i < players.length; i++) {
			if (i != 0) {
				writer.print(" ");
			}
			writer.print(players[i].life);
		}
		writer.println();

		for (String row : field.getStatus()) {
			writer.println(row);
		}

		for (Player player : players) {
			writer.println(player.point.toString());
		}

		writer.println("EOD");
		writer.flush();
	}

	public boolean isThere(int x, int y) {
		return isAlive() && point.isThere(x, y);
	}

	public int getY() {
		return point.getY();
	}

	public int getX() {
		return point.getX();
	}
}

class GetActionThread extends Thread {
	private BufferedReader reader;
	private ArrayList<String> strings;

	public GetActionThread(BufferedReader r, ArrayList<String> s) {
		reader = r;
		strings = s;
	}

	public void run() {
		try {
			String string = reader.readLine();
			strings.add(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class ErrorStreamRedirector extends Thread {
	public BufferedReader reader;

	public ErrorStreamRedirector(InputStream is) {
		reader = new BufferedReader(new InputStreamReader(is));
	}

	public void run() {
		while (true) {
			String s;
			try {
				s = reader.readLine();
			} catch (Exception e) {
				// e.printStackTrace();
				return;
			}
			if (s == null) {
				break;
			}
			System.err.println(s);
		}
	}
}