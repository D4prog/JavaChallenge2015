package net.aicomp.javachallenge2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.exkazuu.gameaiarena.manipulator.Manipulator;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Bookmaker {
	public static final int PLAYERS_NUM = 4;
	private static final int MIN_TIME = 1;
	private static final int READY_TIME_LIMIT = 5000;
	private static final int ACTION_TIME_LIMIT = 2000;

	private static final String EXEC_COMMAND = "a";
	private static final String PAUSE_COMMAND = "p";
	private static final String UNPAUSE_COMMAND = "u";
	private static final String SEED_COMMAND = "s";

	public static void main(String[] args) throws InterruptedException,
			ParseException {
		Options options = buildOptions();

		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine line = parser.parse(options, args);
			if (!hasCompleteArgs(line)) {
				printHelp(options);
				return;
			}
			start(new Game(), line);
		} catch (ParseException e) {
			System.err.println("Error: " + e.getMessage());
			printHelp(options);
			System.exit(-1);
		}
		System.out.println("Game Finished!");
	}

	@SuppressWarnings("unused")
	private static void start(Game game, CommandLine line) {
		String[] execAICommands = line.getOptionValues(EXEC_COMMAND);
		String[] pauseAICommands = line.hasOption(PAUSE_COMMAND) ? line
				.getOptionValues(PAUSE_COMMAND) : new String[PLAYERS_NUM];
		String[] unpauseAICommands = line.hasOption(UNPAUSE_COMMAND) ? line
				.getOptionValues(UNPAUSE_COMMAND) : new String[PLAYERS_NUM];

		Logger.initialize(Logger.LOG_LEVEL_DETAILS);

		List<RunManipulators> ais = new ArrayList<RunManipulators>();
		for (int i = 0; i < PLAYERS_NUM; i++) {
			try {
				ExternalComputerPlayer com = new ExternalComputerPlayer(
						execAICommands[i].split(" "));
				ais.add(new RunManipulators(new AIInitializer(com, i)
						.limittingSumTime(MIN_TIME, READY_TIME_LIMIT),
						new AIManipulator(com, i).limittingSumTime(MIN_TIME,
								ACTION_TIME_LIMIT)));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		play(game, ais, line.getOptionValue(SEED_COMMAND));

	}

	private static void play(Game game, List<RunManipulators> ais, String seed) {
		game.initialize(seed);

		for (RunManipulators ai : ais) {
			ai.getInitializeManipulator().run(game);
		}

		while (!game.isFinished()) {
			int turn = game.getTurn();
			String[] commands = ais.get(turn % PLAYERS_NUM).getRunManipulator()
					.run(game);
			game.processTurn(commands);
		}

	}

	private static void printHelp(Options options) {
		HelpFormatter help = new HelpFormatter();
		help.printHelp("java -jar Bookmaker.jar [OPTIONS]\n" + "[OPTIONS]: ",
				"", options, "", true);
	}

	private static Options buildOptions() {
		return new Options()
				.addOption(
						EXEC_COMMAND,
						true,
						"The command and arguments with double quotation marks to execute AI program (e.g. -a \"java MyAI\")")
				.addOption(
						PAUSE_COMMAND,
						true,
						"The command and arguments with double quotation marks to pause AI program (e.g. -p \"echo pause\")")
				.addOption(
						UNPAUSE_COMMAND,
						true,
						"The command and arguments with double quotation marks to unpause AI program (e.g. -u \"echo unpause\")")
				.addOption(SEED_COMMAND, true, "The seed of the game");
	}

	/**
	 * コマンドライン引数の確認。与えられたコマンドライン引数がnullでなく、実行コマンドは必ず4つ、
	 * ポーズコマンドとアンポーズコマンドは1つも無いか4つ与えられているかを確認する。
	 * 
	 * @param line
	 * @author J.Kobayashi
	 * @return 条件が満たされるならば {@code true}、それ以外の場合は{@code false}
	 */
	private static boolean hasCompleteArgs(CommandLine line) {
		if (line == null) {
			return false;
		}
		if (!line.hasOption(EXEC_COMMAND)
				|| line.getOptionValues(EXEC_COMMAND).length != PLAYERS_NUM) {
			return false;
		}
		return (!line.hasOption(PAUSE_COMMAND) || line
				.getOptionValues(PAUSE_COMMAND).length == PLAYERS_NUM)
				&& (!line.hasOption(UNPAUSE_COMMAND) || line
						.getOptionValues(UNPAUSE_COMMAND).length == PLAYERS_NUM);
	}

}

abstract class GameManipulator extends Manipulator<Game, String[]> {
	protected int _index;

	public GameManipulator(int index) {
		_index = index;
	}
}

class AIInitializer extends GameManipulator {
	private static final String READY = "READY";
	private ExternalComputerPlayer _com;
	private List<String> _lines;

	public AIInitializer(ExternalComputerPlayer com, int index) {
		super(index);
		_com = com;
	}

	@Override
	protected void runPreProcessing(Game game) {
		_lines = new ArrayList<String>();
	}

	@Override
	protected void runProcessing() {
		String line = "";
		do {
			line = _com.readLine();
			if (line != null) {
				line = line.trim();
				_lines.add(line);
			}
		} while (line == null || !line.equals(READY));
	}

	@Override
	protected String[] runPostProcessing() {
		if (!_com.getErrorLog().isEmpty()) {
			Logger.outputLog("AI" + _index + ">>STDERR: " + _com.getErrorLog(),
					Logger.LOG_LEVEL_DETAILS);
		}
		String[] ret = new String[_lines.size()];
		for (String line : _lines) {
			Logger.outputLog("AI" + _index + ">>STDOUT: " + line,
					Logger.LOG_LEVEL_DETAILS);
			ret[_lines.indexOf(line)] = line;
		}
		return ret;
	}
}

class AIManipulator extends GameManipulator {
	private ExternalComputerPlayer _com;
	private String _line;

	AIManipulator(ExternalComputerPlayer com, int index) {
		super(index);
		_com = com;
	}

	@Override
	protected void runPreProcessing(Game game) {
		Logger.outputLog("AI" + _index
				+ ">>Writing to stdin, waiting for stdout",
				Logger.LOG_LEVEL_DETAILS);
		String input = game.getTurnInformation(_index);
		_com.writeLine(input);

		String log = game.getLogInformation(_index);
		Logger.outputLog(log, Logger.LOG_LEVEL_DETAILS);
		_line = "";
	}

	@Override
	protected void runProcessing() {
		_line = _com.readLine();
	}

	@Override
	protected String[] runPostProcessing() {
		if (!_com.getErrorLog().isEmpty()) {
			Logger.outputLog("AI" + _index + ">>STDERR: " + _com.getErrorLog(),
					Logger.LOG_LEVEL_DETAILS);
		}
		Logger.outputLog("AI" + _index + ">>STDOUT: " + _line,
				Logger.LOG_LEVEL_DETAILS);
		String[] ret;
		if (!(_line == null || _line.isEmpty())) {
			ret = _line.trim().split(" ");
		} else {
			ret = new String[0];
		}
		return ret;
	}
}
