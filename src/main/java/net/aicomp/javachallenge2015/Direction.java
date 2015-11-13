package net.aicomp.javachallenge2015;

import java.util.Random;

public enum Direction {
	UP, LEFT, DOWN, RIGHT;

	public static Direction getRandomDir() {
		Random random = new Random(System.currentTimeMillis());
		int dir = random.nextInt(values().length);
		return values()[dir];
	}
}
