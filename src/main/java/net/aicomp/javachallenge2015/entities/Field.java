package net.aicomp.javachallenge2015.entities;

import net.aicomp.javachallenge2015.Player;

public class Field {
	private static final int FIELD_SIZE = 8;
	private Block[][] field;

	public Field() {
		field = new Block[FIELD_SIZE][FIELD_SIZE];
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = new Block();
			}
		}
	}

	public boolean isInside(int x, int y) {
		return 0 <= x && x < FIELD_SIZE * Block.BLOCK_SIZE && 0 <= y
				&& y < FIELD_SIZE * Block.BLOCK_SIZE;
	}

	public String[][] getRawStatus() {
		int size = FIELD_SIZE * Block.BLOCK_SIZE;
		String[][] board = new String[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = Integer.toString(field[i / Block.BLOCK_SIZE][i
						/ Block.BLOCK_SIZE].life);
			}
		}
		return board;
	}

	public String[] getStatus() {
		int size = FIELD_SIZE * Block.BLOCK_SIZE;
		String[] ret = new String[size];
		String[][] board = getRawStatus();
		for (int y = 0; y < size; y++) {
			StringBuilder builder = new StringBuilder();
			for (int x = 0; x < size; x++) {
				if (x > 0) {
					builder.append(' ');
				}
				builder.append(board[y][x]);
			}
			ret[y] = builder.toString();
		}
		return ret;
	}

	public String[] getStatus(Player[] players) {
		int size = FIELD_SIZE * Block.BLOCK_SIZE;
		String[] ret = new String[size];
		String[][] board = getRawStatus();
		for (int i = 0; i < players.length; i++) {
			char name = (char) ('A' + i);
			board[players[i].getY()][players[i].getX()] = name + "" + name;
		}
		for (int y = 0; y < size; y++) {
			StringBuilder builder = new StringBuilder();
			for (int x = 0; x < size; x++) {
				if (x > 0) {
					builder.append(' ');
				}
				builder.append(board[y][x]);
			}
			ret[y] = builder.toString();
		}
		return ret;
	}

}

class Block {
	int life;
	static final int BLOCK_SIZE = 5;

	public Block() {
		life = 0;
	}

	public int getLife() {
		return life;
	}
}
