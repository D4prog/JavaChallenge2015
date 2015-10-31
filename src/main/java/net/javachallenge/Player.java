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
    public int x = -1, y = -1;
    public int rebirthTurn = 0;
    private int mutekiTurn = 0;
    private int pauseTurn = 0;
    public int dir = 0;

    private boolean onBoard;

    public Player(int life, String exec) {
	this.life = life;
	onBoard = false;// 一番最初は載ってない
	this.execCommand = exec;
	if (exec.length() == 0) {
	    life = 0;
	} else if (!runPlayer()) {
	    life = 0;
	}
    }

    // AIを開始する
    private boolean runPlayer() {
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

	// 末尾にEODを出力
	writer.println("EOD");
	writer.flush();
    }

    // コマンドを受け取る
    public String getAction() {
	if (!isAlive()) {
	    return Bookmaker.NONE;
	}
	try {
	    String string = reader.readLine();
	    return string;
	} catch (IOException e) {
	    stopSolution();
	    return Bookmaker.NONE;
	}
    }

    // AIを終了する
    public void stopSolution() {
	life = 0;
	if (aiProcess != null) {
	    try {
		aiProcess.destroy();
	    } catch (Exception e) {
		// do nothing
	    }
	}
    }

    // 落とす
    public void drop(int turn) {
	life--;
	onBoard = false;
	rebirthTurn = Bookmaker.PLAYER_REBIRTH_TURN + turn;
	if (life == 0) {
	    stopSolution();
	}
    }

    // 生きているか（プレイ続行可能か）
    public boolean isAlive() {
	return life > 0;
    }

    // 生きてボードの上に乗っているか
    public boolean isOnBoard() {
	return isAlive() && onBoard;
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
	onBoard = true;
	moveTo(x, y);
	if (turn > 0) {
	    // turn=0の時は初期配置なので無敵にならない
	    mutekiTurn = turn + Bookmaker.MUTEKI_TURN;
	}
    }

    // (x,y)に移動させる
    public void moveTo(int x, int y) {
	this.x = x;
	this.y = y;
    }

    // 向きを決める
    public void directTo(String s) {
	for (int i = 0; i < Bookmaker.DIRECTION.length; i++) {
	    if (s.equals(Bookmaker.DIRECTION[i])) {
		dir = i;
	    }
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