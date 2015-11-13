package net.aicomp.javachallenge2015.entities;

public class Point {
	int x, y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return x + " " + y;
	}

	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isThere(int x, int y) {
		return this.x == x && this.y == y;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public boolean isPositive() {
		return x >= 0 && y >= 0;
	}
}
