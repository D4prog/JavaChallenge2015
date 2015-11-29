package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Field {
	private static final int MAX_COORD = Constants.FIELD_SIZE * Constants.BLOCK_SIZE;
	private final Block[][] _field;

	public Field() {
		_field = new Block[Constants.FIELD_SIZE][Constants.FIELD_SIZE];
		for (int i = 0; i < _field.length; i++) {
			for (int j = 0; j < _field[i].length; j++) {
				_field[i][j] = new Block(j, i);
			}
		}
	}

	public boolean isInside(int x, int y) {
		return 0 <= x && x < Field.MAX_COORD && 0 <= y && y < Field.MAX_COORD;
	}

	public StringBuilder serialize(StringBuilder builder) {
		for (int y = 0; y < Constants.FIELD_SIZE; y++) {
			String delimiter = "";
			for (int x = 0; x < Constants.FIELD_SIZE; x++) {
				builder.append(delimiter);
				builder.append(_field[y][x]._life);
				delimiter = " ";
			}
			builder.append(Constants.LineSeparator);
		}
		return builder;
	}

	public void dropBlocks(int x, int y, Direction4 dir) {
		int blcx = x / Constants.BLOCK_SIZE;
		int blcy = y / Constants.BLOCK_SIZE;
		Point2 start = new Point2(blcx, blcy);
		Point2 current = dir.move(start);
		while (0 <= current.x && 0 <= current.y && current.x < Constants.FIELD_SIZE
				&& current.y < Constants.FIELD_SIZE) {
			int dist = current.getManhattanDistance(start);
			_field[current.y][current.x].setLife(dist * Constants.PLAYERS_NUM);
			current = dir.move(current);
		}
	}

	public boolean canMove(Point2 point) {
		if (!isInside(point.x, point.y)) {
			return false;
		}
		int blcx = point.x / Constants.BLOCK_SIZE;
		int blcy = point.y / Constants.BLOCK_SIZE;
		return _field[blcy][blcx].isAlive();
	}

	public void refresh(Player[] players) {
		for (int y = 0; y < Constants.FIELD_SIZE; y++) {
			for (int x = 0; x < Constants.FIELD_SIZE; x++) {
				Block block = _field[y][x];
				List<Player> playersInBlock = new ArrayList<Player>();
				for (Player player : players) {
					if (player.isThere(block._area)) {
						playersInBlock.add(player);
					}
				}
				block.refresh(playersInBlock);
			}
		}
	}

	public Set<Point2> getSpawnablePoints(Player[] players) {
		Set<Point2> ret = new HashSet<Point2>();
		for (Block[] row : _field) {
			for (Block block : row) {
				if (block.isAlive()) {
					ret.addAll(block._area);
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
		private Set<Point2> _area;
		private int _life;

		private Block(int x, int y) {
			_area = new HashSet<Point2>(Point2.getPoints(x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE,
					(x + 1) * Constants.BLOCK_SIZE, (y + 1) * Constants.BLOCK_SIZE));
			_life = 0;
		}

		private void refresh(List<Player> players) {
			if (_life > 0) {
				_life--;
				if (_life == 0) {
					_life = -Constants.BLOCK_REBIRTH_TIME * Constants.PLAYERS_NUM + 1;
					for (Player player : players) {
						player.fall();
					}
				}
			} else if (_life < 0) {
				_life++;
			}
		}

		private void setLife(int i) {
			if (_life == 0) {
				_life = i;
			}
		}

		private boolean isAlive() {
			return _life >= 0;
		}
	}
}