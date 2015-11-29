package net.aicomp.javachallenge2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import net.aicomp.javachallenge2015.log.Logger;
import net.aicomp.javachallenge2015.manipulators.AIInitializer;
import net.aicomp.javachallenge2015.manipulators.AIRunner;
import net.aicomp.javachallenge2015.manipulators.ManipulatorSet;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

public class Bookmaker {
	private static final String EXEC_COMMAND = "a";
	private static final String PAUSE_COMMAND = "p";
	private static final String UNPAUSE_COMMAND = "u";
	private static final String SEED_COMMAND = "s";
	private static final String TURN_COMMAND = "t";

	public static void main(String[] args) throws InterruptedException, ParseException {
		Options options = buildOptions();

		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine line = parser.parse(options, args);
			if (!hasCompleteArgs(line)) {
				printHelp(options);
				return;
			}
			start(line);
		} catch (ParseException e) {
			System.err.println("Error: " + e.getMessage());
			printHelp(options);
			System.exit(-1);
		} finally {
			Logger.get().writeLog("Game Finished!");
			// must exit to clear all resources including threads
			System.exit(0);
		}
	}

	public static void start(CommandLine line) {
		List<ManipulatorSet> ais = initializeManipulatorSets(line);
		play(ais, line.getOptionValue(SEED_COMMAND), line.getOptionValue(TURN_COMMAND));
	}

	private static List<ManipulatorSet> initializeManipulatorSets(CommandLine line) {
		String[] execAICommands = line.getOptionValues(EXEC_COMMAND);
		String[] pauseAICommands = line.hasOption(PAUSE_COMMAND) ? line.getOptionValues(PAUSE_COMMAND)
				: new String[Constants.PLAYERS_NUM];
		String[] unpauseAICommands = line.hasOption(UNPAUSE_COMMAND) ? line.getOptionValues(UNPAUSE_COMMAND)
				: new String[Constants.PLAYERS_NUM];
		for (int i = 0; i < pauseAICommands.length; i++) {
			if (pauseAICommands[i] == null) {
				pauseAICommands[i] = "";
			}
		}
		for (int i = 0; i < unpauseAICommands.length; i++) {
			if (unpauseAICommands[i] == null) {
				unpauseAICommands[i] = "";
			}
		}
		List<ManipulatorSet> ais = new ArrayList<ManipulatorSet>();
		for (int i = 0; i < Constants.PLAYERS_NUM; i++) {
			try {
				ExternalComputerPlayer com = new ExternalComputerPlayer(execAICommands[i].split(" "));
				String[] pauseAICommandWithArgs = pauseAICommands[i].split(" ");
				String[] unpauseAICommandWithArgs = unpauseAICommands[i].split(" ");
				ais.add(new ManipulatorSet(
						new AIInitializer(com).limittingTime(Constants.READY_TIME_LIMIT).ignoringException()
								.pauseUnpause(pauseAICommandWithArgs, unpauseAICommandWithArgs),
						new AIRunner(com).limittingTime(Constants.ACTION_TIME_LIMIT).ignoringException()
								.pauseUnpause(pauseAICommandWithArgs, unpauseAICommandWithArgs)));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return ais;
	}

	private static void play(List<ManipulatorSet> ais, String seed, String maxTurn) {
		Game game = new Game(seed, maxTurn);
		for (ManipulatorSet ai : ais) {
			ai.getInitializer().run(game);
			if (ai.getInitializer().released()) {
				ai.getRunner().release();
			}
			ai.getRunner().setPaused(ai.getInitializer().paused());
		}

		while (!game.isFinished()) {
			int index = game.getCurrentPlayerIndex();
			String command = ais.get(index).getRunner().run(game);
			game.processTurn(command);
		}
		game.finish();
	}

	private static void printHelp(Options options) {
		HelpFormatter help = new HelpFormatter();
		help.printHelp("java -jar Bookmaker.jar [OPTIONS]\n" + "[OPTIONS]: ", "", options, "", true);
	}

	public static Options buildOptions() {
		return new Options()
				.addOption(EXEC_COMMAND, true,
						"The command and arguments with double quotation marks to execute AI program (e.g. -a \"java MyAI\")")
				.addOption(PAUSE_COMMAND, true,
						"The command and arguments with double quotation marks to pause AI program (e.g. -p \"echo pause\")")
				.addOption(UNPAUSE_COMMAND, true,
						"The command and arguments with double quotation marks to unpause AI program (e.g. -u \"echo unpause\")")
				.addOption(SEED_COMMAND, true, "The seed of the game")
				.addOption(TURN_COMMAND, true, "The turn number of game end");
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
		if (!line.hasOption(EXEC_COMMAND) || line.getOptionValues(EXEC_COMMAND).length != Constants.PLAYERS_NUM) {
			return false;
		}
		return (!line.hasOption(PAUSE_COMMAND) || line.getOptionValues(PAUSE_COMMAND).length == Constants.PLAYERS_NUM)
				&& (!line.hasOption(UNPAUSE_COMMAND)
						|| line.getOptionValues(UNPAUSE_COMMAND).length == Constants.PLAYERS_NUM);
	}
}
