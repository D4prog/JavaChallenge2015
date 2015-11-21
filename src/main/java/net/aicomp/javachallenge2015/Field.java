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
				field[i][j] = new Block(j, i);
			}
		}
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
					if (player.isThere(block.area)) {
						playersInBlock.add(player);
					}
				}
				block.refresh(playersInBlock);
			}
		}
	}

	public Set<Point2> getSpawnablePoints(Player[] players) {
		Set<Point2> ret = new HashSet<Point2>();
		for (Block[] row : field) {
			for (Block block : row) {
				if (block.isAlive()) {
					ret.addAll(block.area);
				}
			}
		}
		for (Point2 point : ret) {
			for (Player player : players) {
				if (player != null && player.isCollided(point)) {
					ret.remove(point);
				}
			}
		}
		return ret;
	}

	private static class Block {
		private int life;
		private static final int REBIRTH_TIME = 5;
		private Set<Point2> area;

		private Block(int x, int y) {
			area = new HashSet<Point2>(Point2.getPoints(x * Field.BLOCK_SIZE, y
					* Field.BLOCK_SIZE, (x + 1) * Field.BLOCK_SIZE, (y + 1)
					* Field.BLOCK_SIZE));
			life = 0;
		}

		private void refresh(List<Player> players) {
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

		private void setLife(int i) {
			if (life == 0) {
				life = i;
			}
		}

		private boolean isAlive() {
			return life >= 0;
		}
	}

}