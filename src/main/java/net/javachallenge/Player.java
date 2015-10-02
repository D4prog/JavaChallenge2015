package net.javachallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Player {
	private String execCommand = null;
	private Process aiProcess;
	private BufferedReader reader;
	private PrintWriter writer;

	public int life;
	public int x, y;
	public boolean onBoard;

	public Player(int life, String exec) {
		this.life = life;
		onBoard = true;
		this.execCommand = exec;
		runPlayer();
	}

	// AIを開始する
	public boolean runPlayer() {
		aiProcess = null;

		try {
			aiProcess = Runtime.getRuntime().exec(execCommand);
		} catch (Exception e) {
			System.err
					.println("ERROR: Unable to execute your solution using the provided command: "
							+ execCommand + ".");
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
		return false;
	}

	// AIに情報を送る
	public void putInformation(int playerID, int turn, int[][] board,
			ArrayList<Integer> lifeList, ArrayList<String> whereList) {
		// プレイヤーID・ターン数を出力
		writer.println(playerID);
		writer.println(turn);

		// 残機を出力
		for (Integer life : lifeList) {
			writer.println(life);
		}

		// ボード情報を出力
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (j != 0) {
					writer.print(" ");
				}
				writer.print(board[i][j]);
			}
			writer.println();
		}

		// 各プレーヤーの場所を出力
		for (String string : whereList) {
			writer.println(string);
		}

		writer.flush();
	}

	// コマンドを受け取る
	public String getAction() {
		if (!isAlive()) {
			return "N";
		}
		try {
			String string = reader.readLine();
			return string;
		} catch (IOException e) {
			stopSolution();
			return "N";
		}
	}

	// AIを終了する
	public void stopSolution() {
		if (aiProcess != null) {
			try {
				aiProcess.destroy();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	public void drop() {
		life--;
	}

	public boolean isAlive() {
		return aiProcess != null && life > 0;
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