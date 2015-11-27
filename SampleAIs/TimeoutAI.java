import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

// sample timeout AI
public class TimeoutAI {
	private static Random random;
	private static Scanner scanner;
	private static PrintWriter writer;

	private static int playerId;
	private static int T;
	private static int B[][];
	private static int R[];
	private static int C[];
	private static String D[];
	private static int S[];
	private static String EOD;

	private static int myLife;
	private static int enemyLives;
	private static String commands = "URDLAN";

	public static void main(String[] args) {
		B = new int[6][6];
		R = new int[4];
		C = new int[4];
		D = new String[4];
		S = new int[4];

		if (args.length >= 2) {
			if (args[0].equals("-s")) {
				random = new Random(Long.parseLong(args[1]));
			}
		} else {
			random = new Random();
		}
		scanner = new Scanner(System.in);
		writer = new PrintWriter(System.out, true);
		writer.println("READY");
		writer.flush();

		while (true) {
			readData();

			enemyLives = 0;
			for (int i = 0; i < 4; i++) {
				if (i != playerId && !isDead(i)) {
					enemyLives++;
				}
			}
			if (isDead(playerId) || enemyLives == 0) {
				break;
			}

			writeCommand();
		}

		scanner.close();
		writer.close();
	}

	private static boolean isDead(int id) {
		return R[id] == -1 && C[id] == -1;
	}

	private static void readData() {
		playerId = scanner.nextInt();
		T = scanner.nextInt();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				B[i][j] = scanner.nextInt();
			}
		}
		for (int i = 0; i < 4; i++) {
			R[i] = scanner.nextInt();
			C[i] = scanner.nextInt();
			D[i] = scanner.next();
			S[i] = scanner.nextInt();
		}
		EOD = scanner.next();
	}

	private static void writeCommand() {
		try {
			Thread.sleep(T * 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.println(commands.charAt(random.nextInt(commands.length())));
		writer.flush();
	}
}