import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class SampleAI {
    private final int BOARD_W = 18;
    private final int FIELD_W = 6;
    private final int BLOCK_W = BOARD_W / FIELD_W;
    private final int PLAYER_N = 4;

    private final char ATTACK = 'A';
    private final char UP = 'U';
    private final char RIGHT = 'R';
    private final char DOWN = 'D';
    private final char LEFT = 'L';
    private final char NONE = 'N';
    private final char[] ACTIONS = { ATTACK, UP, DOWN, RIGHT, LEFT };

    private int id = 0, turn = 0;
    private int[][] board = new int[FIELD_W][FIELD_W];
    private int[] lifes = new int[PLAYER_N];
    private int[] row = new int[PLAYER_N];
    private int[] column = new int[PLAYER_N];
    private char dir[] = new char[PLAYER_N];
    private int[] pausing = new int[PLAYER_N];

    private char action() {
	for (int i = 0; i < PLAYER_N; i++) {
	    if (i == id || board[i][0] < 0 || board[i][1] < 0)
		continue;
	    if (board[i][0] == board[id][0]) {
		if (board[i][1] < board[id][1] && dir[id] == LEFT) {
		    return ATTACK;
		} else if (board[i][1] > board[id][1] && dir[id] == RIGHT) {
		    return ATTACK;
		}
	    } else if (board[i][1] == board[id][1]) {
		if (board[i][0] < board[id][0] && dir[id] == UP) {
		    return ATTACK;
		} else if (board[i][0] > board[id][0] && dir[id] == DOWN) {
		    return ATTACK;
		}
	    }
	}

	for (int i = 0; i < PLAYER_N; i++) {
	    if (i == id || board[i][0] < 0 || board[i][1] < 0)
		continue;
	    if (board[i][0] == board[id][0]) {
		if (board[i][1] < board[id][1]) {
		    return LEFT;
		} else if (board[i][1] > board[id][1]) {
		    return RIGHT;
		}
	    } else if (board[i][1] == board[id][1]) {
		if (board[i][0] < board[id][0]) {
		    return UP;
		} else if (board[i][0] > board[id][0]) {
		    return DOWN;
		}
	    }
	}

	int[] blockDist = new int[PLAYER_N];
	for (int i = 0; i < PLAYER_N; i++) {
	    if (i == id || board[i][0] < 0 || board[i][1] < 0)
		continue;
	    blockDist[i] = Math.min(Math.abs(board[id][0] - board[i][0]),
		    Math.abs(board[id][1] - board[i][1]));
	}
	int near = id;
	for (int i = 0; i < PLAYER_N; i++) {
	    if (i == id || board[i][0] < 0 || board[i][1] < 0)
		continue;
	    if (near == id || blockDist[near] > blockDist[i]) {
		near = i;
	    }
	}
	if (near == id) {
	    return ATTACK;
	}

	if (Math.abs(board[near][0] - board[id][0]) > Math.abs(board[near][1]
		- board[id][1])) {
	    if (board[id][1] > board[near][1]) {
		return LEFT;
	    } else {
		return RIGHT;
	    }
	} else {
	    if (board[id][0] > board[near][0]) {
		return DOWN;
	    } else {
		return UP;
	    }
	}
    }

    public void run() {
	System.out.println("READY");
	Scanner scanner = new Scanner(System.in);

	while (true) {
	    id = scanner.nextInt();
	    turn = scanner.nextInt();
	    for (int i = 0; i < lifes.length; i++) {
		lifes[i] = scanner.nextInt();
	    }

	    for (int i = 0; i < board.length; i++) {
		for (int j = 0; j < board[i].length; j++) {
		    board[i][j] = scanner.nextInt();
		}
	    }
	    for (int i = 0; i < PLAYER_N; i++) {
		row[i] = scanner.nextInt();
		column[i] = scanner.nextInt();
		dir[i] = scanner.next().toCharArray()[0];
		pausing[i] = scanner.nextInt();
	    }
	    scanner.next();// EOD

	    System.out.println(action());
	}
    }

    public static void main(String[] args) {
	new SampleAI().run();
    }

}
