import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class SampleAI {
    private final int BOARD_W = 40;
    private final int FIELD_W = 8;
    private final int BLOCK_W = BOARD_W / FIELD_W;
    private final int PLAYER_N = 4;
    private int id = 0, turn = 0;
    private int[] lifes = new int[PLAYER_N];
    private int[] row = new int[PLAYER_N];
    private int[] column = new int[PLAYER_N];
    private int[][] board = new int[BOARD_W][BOARD_W];

    private int[] lastAttackTurn = new int[PLAYER_N];
    private int[][] field = new int[FIELD_W][FIELD_W];
    private final char ATTACK = 'A';
    private final char UP = 'U';
    private final char RIGHT = 'R';
    private final char DOWN = 'D';
    private final char LEFT = 'L';
    private final char NONE = 'N';
    private final char[] ACTIONS = { ATTACK, UP, DOWN, RIGHT, LEFT };
    private char dir = NONE;

    private Random random = new Random();

    private char action() {
	int[][] block = new int[PLAYER_N][2];
	for (int i = 0; i < PLAYER_N; i++) {
	    block[i][0] = column[i] / BLOCK_W;
	    block[i][1] = row[i] / BLOCK_W;
	}

	for (int i = 0; i < PLAYER_N; i++) {
	    if (i == id || block[i][0] < 0 || block[i][1] < 0)
		continue;
	    if (block[i][0] == block[id][0]) {
		if (block[i][1] < block[id][1] && dir == LEFT) {
		    return ATTACK;
		} else if (block[i][1] > block[id][1] && dir == RIGHT) {
		    return ATTACK;
		}
	    } else if (block[i][1] == block[id][1]) {
		if (block[i][0] < block[id][0] && dir == UP) {
		    return ATTACK;
		} else if (block[i][0] > block[id][0] && dir == DOWN) {
		    return ATTACK;
		}
	    }
	}

	for (int i = 0; i < PLAYER_N; i++) {
	    if (i == id || block[i][0] < 0 || block[i][1] < 0)
		continue;
	    if (block[i][0] == block[id][0]) {
		if (block[i][1] < block[id][1]) {
		    return LEFT;
		} else if (block[i][1] > block[id][1]) {
		    return RIGHT;
		}
	    } else if (block[i][1] == block[id][1]) {
		if (block[i][0] < block[id][0]) {
		    return UP;
		} else if (block[i][0] > block[id][0]) {
		    return DOWN;
		}
	    }
	}
	int rnd = random.nextInt(turn);
	return ACTIONS[rnd % ACTIONS.length];
    }

    public void run() {
	System.out.println("READY");
	Scanner scanner = new Scanner(System.in);
	Arrays.fill(lastAttackTurn, -1);

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
	    }
	    scanner.next();// EOD

	    for (int i = 0; i < FIELD_W; i++) {
		for (int j = 0; j < FIELD_W; j++) {
		    field[i][j] = board[i * 5][j * 5];
		}
	    }
	    System.out.println(action());
	}

    }

    public static void main(String[] args) {
	new SampleAI().run();
    }

}
