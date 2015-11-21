import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

// sample random-walk AI
public class SampleAIL {
	private static Random random;
	private static Scanner scanner;
	private static PrintWriter writer;

	private static int playerId;
	private static int T;
	private static int L[];
	private static int B[][];
	private static int R[];
	private static int C[];
	private static String D[];
	private static String O[];
	private static String EOD;

	private static int myLife;
	private static int enemyLives;
	private static String commands = "URDLAN";

	public static void main(String[] args) {
		L = new int[4];
		B = new int[40][40];
		R = new int[4];
		C = new int[4];
		D = new String[4];
		O = new String[4];

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

			myLife = L[playerId];
			enemyLives = 0;
			for (int i = 0; i < 4; i++) {
				if (i != playerId) {
					enemyLives += L[i];
				}
			}
			if (myLife == 0 || enemyLives == 0) {
				break;
			}

			writeCommand();
		}

		scanner.close();
		writer.close();
	}

	private static void readData() {
		playerId = scanner.nextInt();
		T = scanner.nextInt();
		for (int i = 0; i < 4; i++) {
			L[i] = scanner.nextInt();
		}
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				B[i][j] = scanner.nextInt();
			}
		}
		for (int i = 0; i < 4; i++) {
			R[i] = scanner.nextInt();
			C[i] = scanner.nextInt();
			// D[i] = scanner.next();
		}
		for (int i = 0; i < 4; i++) {
			// O[i] = scanner.next();
		}
		EOD = scanner.next();
	}

	private static void writeCommand() {
		writer.println('L');
		writer.flush();
	}
}