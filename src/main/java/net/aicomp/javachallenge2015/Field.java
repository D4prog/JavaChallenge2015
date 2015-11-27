package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Field {
	private static final int FIELD_SIZE = 6;
	private static final int BLOCK_SIZE = 3;
	private static final int MAX_COORD = FIELD_SIZE * BLOCK_SIZE;
	private final Block[][] field;

	public Field() {
		field = new Block[FIELD_SIZE][FIELD_SIZE];
		for (int y = 0; y < field.length; y++) {
			for (int x = 0; x < field[y].length; x++) {
				field[y][x] = new Block(x, y);
			}
		}
	}

	public boolean isInside(int x, int y) {
		return 0 <= x && x < MAX_COORD && 0 <= y && y < MAX_COORD;
	}

	public List<String> getBlockStatus() {
		List<String> ret = new ArrayList<String>();
		for (int y = 0; y < FIELD_SIZE; y++) {
			StringBuilder builder = new StringBuilder();
			String delimiter = "";
			for (int x = 0; x < FIELD_SIZE; x++) {
				builder.append(delimiter);
				builder.append(field[y][x].life);
				delimiter = " ";
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
		while (0 <= current.x && 0 <= current.y && current.x < FIELD_SIZE && current.y < FIELD_SIZE) {
			int dist = current.getManhattanDistance(start);
			field[current.y][current.x].setLife(dist * Bookmaker.PLAYERS_NUM);
			current = dir.move(current);
		}
	}

	public boolean canMove(Point2 point) {
		if (!isInside(point.x, point.y)) {
			return false;
		}
		int blcx = point.x / BLOCK_SIZE;
		int blcy = point.y / BLOCK_SIZE;
		return field[blcy][blcx].isAlive();
	}

	public void refresh(Player[] players) {
		for (int y = 0; y < FIELD_SIZE; y++) {
			for (int x = 0; x < FIELD_SIZE; x++) {
				Block block = field[y][x];
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
		for (Point2 point : ret.toArray(new Point2[0])) {
			for (Player player : players) {
				if (player != null && player.isCollided(point)) {
					ret.remove(point);
				}
			}
		}
		return ret;
	}

	private static class Block {
		private static final int REBIRTH_TIME = 5;
		private Set<Point2> area;
		private int life;

		private Block(int x, int y) {
			area = new HashSet<Point2>(Point2.getPoints(x * Field.BLOCK_SIZE, y * Field.BLOCK_SIZE,
					(x + 1) * Field.BLOCK_SIZE, (y + 1) * Field.BLOCK_SIZE));
			life = 0;
		}

		private void refresh(List<Player> players) {
			if (life > 0) {
				life--;
				if (life == 0) {
					life = -REBIRTH_TIME * Bookmaker.PLAYERS_NUM + 1;
					for (Player player : players) {
						player.fall();
					}
				}
			} else if (life < 0) {
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