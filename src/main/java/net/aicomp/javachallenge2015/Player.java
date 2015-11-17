package net.aicomp.javachallenge2015;

import java.util.Random;

import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final int INITIAL_LIFE = 5;
	private static final int PLAYER_REBIRTH_TURN = 5 * Bookmaker.PLAYERS_NUM;
	private static final int ATTACKED_PAUSE_TURN = 5 * Bookmaker.PLAYERS_NUM;
	private static final int INVINCIBLE_TURN = 10 * Bookmaker.PLAYERS_NUM;

	public int life;
	private Point2 point;
	public int rebirthTurn = 0;
	private int invincibleTurn = -1;
	private int pauseTurn = -1;
	private Direction4 dir;

	private ICommand command;

	public Player() {
		this.life = INITIAL_LIFE;
	}

	// 落とす
	public void drop(int turn) {
		life--;
		rebirthTurn = PLAYER_REBIRTH_TURN + turn;
	}

	// 生きているか（プレイ続行可能か）
	public boolean isAlive() {
		return life > 0;
	}

	// 生きてボードの上に乗っているか
	public boolean isOnBoard() {
		return isAlive() && point.x >= 0 && point.y >= 0;
	}

	// 硬直中か
	public boolean isPausing(int turn) {
		return pauseTurn >= turn;
	}

	// 攻撃後の硬直
	public void attackedPause(int turn) {
		pauseTurn = turn + ATTACKED_PAUSE_TURN;
	}

	// 無敵状態か
	public boolean isInvincible(int turn) {
		return invincibleTurn >= turn;
	}

	// (x,y)に復活させる
	public void reBirthOn(int x, int y, int turn) {
		moveTo(x, y);
		if (turn > 0) {
			// turn=0の時は初期配置なので無敵にならない
			invincibleTurn = turn + INVINCIBLE_TURN;
		}
	}

	// (x,y)に移動させる
	public void moveTo(int x, int y) {
		point.add(new Point2(x - point.x, y - point.y));
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
		Random rnd = new Random();
		int d = rnd.nextInt(Direction4.values().length);
		dir = Direction4.values()[d];
	}

	public boolean isThere(int x, int y) {
		return isAlive() && point.equals(new Point2(x, y));
	}

	public int getY() {
		return point.y;
	}

	public int getX() {
		return point.x;
	}

	public Direction4 getDir() {
		return dir;
	}
}
