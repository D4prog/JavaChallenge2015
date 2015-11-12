import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

// sample random-walk AI
public class SampleAI {
	private static Random random;
	private static Scanner scanner;
	private static PrintWriter writer;
	
	private static int T;
	private static int L[4];
	private static int B[40][40];
	private static int R[4];
	private static int C[4];
	private static String D[4];
	private static String O[4];
	
	private static int maxTurn = 100; // temporary magic number
	private static String commands = "URDLAN";

	public static void main(String[] args) {
		random = new Random();
		scanner = new Scanner(System.in);
		writer = new PrintWriter(System.out, true);
		writer.println("Ready");
		writer.flush;

		// TODO: Modify the exit condition
		for(int t = 0; t < maxTurn; t++) {
			readData();
			writeCommand();
		}
		
		scanner.close();
		writer.close();
	}
	
	private static void readData() {
		T = scanner.nextInt();
		for(int i = 0; i < 4; i++) {
			L[i] = scanner.nextInt();
		}
		for(int i = 0; i < 40; i++) {
			for(int j = 0; j < 40; j++) {
				B[i][j] = scanner.nextInt();
			}
		}
		for(int i = 0; i < 4; i++) {
			R[i] = scanner.nextInt();
			C[i] = scanner.nextInt();
			D[i] = scanner.next();
		}
		for(int i = 0; i < 4; i++) {
			O[i] = scanner.next();
		}
	}
	
	private static void writeCommand() {
		writer.println(commands.charAt(random.nextInt(commands.length())));
		writer.flush();
	}
}