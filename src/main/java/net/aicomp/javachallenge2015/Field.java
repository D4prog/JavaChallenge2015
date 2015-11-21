package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Field {
	private static final int FIELD_SIZE = 8;
	static final int BLOCK_SIZE = 5;
	private Block[][] field;

	public Field() {
		field = new Block[FIELD_SIZE][FIELD_SIZE];
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = new Block();
			}
		}
	}

	public static List<Point2> getAllPoints() {
		return Point2.getPoints(FIELD_SIZE * BLOCK_SIZE, FIELD_SIZE
				* BLOCK_SIZE);

	}

	public boolean isInside(int x, int y) {
		return 0 <= x && x < FIELD_SIZE * BLOCK_SIZE && 0 <= y
				&& y < FIELD_SIZE * BLOCK_SIZE;
	}

	private String[][] getRawStatus() {
		int size = FIELD_SIZE * BLOCK_SIZE;
		String[][] board = new String[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = Integer.toString(field[i / BLOCK_SIZE][j
						/ BLOCK_SIZE].life);
			}
		}
		return board;
	}

	public List<String> getStatus() {
		int size = FIELD_SIZE * BLOCK_SIZE;
		List<String> ret = new ArrayList<String>();
		String[][] board = getRawStatus();
		for (int y = 0; y < size; y++) {
			StringBuilder builder = new StringBuilder();
			for (int x = 0; x < size; x++) {
				if (x > 0) {
					builder.append(' ');
				}
				builder.append(board[y][x]);
			}
			ret.add(builder.toString());
		}
		return ret;
	}

	public void setLimit(int x, int y, Direction4 dir) {
		int blcx = x / BLOCK_SIZE;
		int blcy = y / BLOCK_SIZE;
		Point2 start = new Point2(blcx, blcy);
		Point2 current = dir.move(start);
		while (current.x >= 0 && current.y >= 0 && current.x < FIELD_SIZE
				&& current.y < FIELD_SIZE) {
			int dist = current.getManhattanDistance(start);
			field[current.y][current.x].setLife(dist * Bookmaker.PLAYERS_NUM);
			current = dir.move(current);
		}
	}

	public boolean canMove(Point2 point) {
		int size = FIELD_SIZE * BLOCK_SIZE;
		if (point.x < 0 || point.y < 0 || point.x >= size || point.y >= size) {
			return false;
		}
		int blcx = point.x / BLOCK_SIZE;
		int blcy = point.y / BLOCK_SIZE;
		return field[blcy][blcx].isAlive();
	}

	public void refresh(Player[] players) {
		for (int i = 0; i < FIELD_SIZE; i++) {
			for (int j = 0; j < FIELD_SIZE; j++) {
				Block block = field[i][j];
				List<Player> playersInBlock = new ArrayList<Player>();
				for (Player player : players) {
					if (player.isThere(j * BLOCK_SIZE, i * BLOCK_SIZE, (j + 1)
							* BLOCK_SIZE, (i + 1) * BLOCK_SIZE)) {
						playersInBlock.add(player);
					}
				}
				block.refresh(playersInBlock);
			}
		}
	}

	public List<Point2> getSpawnablePoints(Player[] players) {
		List<Point2> ret = Point2.getPoints(FIELD_SIZE * BLOCK_SIZE, FIELD_SIZE
				* BLOCK_SIZE);
		Set<Point2> invalidPoints = new HashSet<Point2>();
		for (int i = 0; i < FIELD_SIZE; i++) {
			for (int j = 0; j < FIELD_SIZE; j++) {
				Block block = field[i][j];
				if (!block.isAlive()) {
					invalidPoints.addAll(Point2.getPoints(j * BLOCK_SIZE, i
							* BLOCK_SIZE, (j + 1) * BLOCK_SIZE, (i + 1)
							* BLOCK_SIZE));
				}
			}
		}
		for (Point2 point : ret) {
			for (Player player : players) {
				if (player != null && player.isCollided(point)) {
					invalidPoints.add(point);
				}
			}
		}
		ret.removeAll(invalidPoints);
		return ret;
	}
}

class Block {
	int life;
	private static final int REBIRTH_TIME = 5;

	Block() {
		life = 0;
	}

	public void refresh(List<Player> players) {
		if (life > 0) {
			life--;
			if (life == 0) {
				life = -REBIRTH_TIME * Bookmaker.PLAYERS_NUM;
				for (Player player : players) {
					if (!player.isInvincible()) {
						player.fall();
					}
				}
			}
		}
		if (life < 0) {
			life++;
		}
	}

	void setLife(int i) {
		if (life == 0) {
			life = i;
		}
	}

	int getLife() {
		return life;
	}

	boolean isAlive() {
		return life >= 0;
	}
}
