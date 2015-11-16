package net.aicomp.javachallenge2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import net.exkazuu.gameaiarena.manipulator.Manipulator;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public class Main {
	private final static String HELP = "h";

	private final static String LOG_LEVEL = "l";

	private final static String SILENT = "s";

	private final static String EXTERNAL_AI_PROGRAM = "a";

	private final static String WORK_DIR_AI_PROGRAM = "w";

	private final static String DEFAULT_COMMAND = "java SampleAI";

	private final static String DEFAULT_WORK_DIR = "./defaultai";

	public static Options buildOptions() {
		Options _xblockexpression = null;
		{
			OptionBuilder.hasArgs();
			OptionBuilder
					.withDescription("Set 1-4 AI players with external programs.");
			final Option externalAIOption = OptionBuilder
					.create(Main.EXTERNAL_AI_PROGRAM);
			OptionBuilder.hasArgs();
			OptionBuilder
					.withDescription("Set working directories for external programs.");
			final Option workDirOption = OptionBuilder
					.create(Main.WORK_DIR_AI_PROGRAM);
			Options _options = new Options();
			Options _addOption = _options.addOption(Main.HELP, false,
					"Print this help.");
			Options _addOption_1 = _addOption
					.addOption(
							Main.LOG_LEVEL,
							true,
							"Specify the log level. 0: Show only result 1: Show game status 2: Show detailed log (defaults to 2)");
			Options _addOption_2 = _addOption_1.addOption(Main.SILENT, false,
					"Disable writing a log file.");
			Options _addOption_3 = _addOption_2.addOption(externalAIOption);
			final Options options = _addOption_3.addOption(workDirOption);
			_xblockexpression = options;
		}
		return _xblockexpression;
	}

	public static void printHelp(final Options options) {
		final HelpFormatter help = new HelpFormatter();
		help.printHelp(("java -jar AILovers.jar [OPTIONS]\n" + "[OPTIONS]: "),
				"", options, "", true);
	}

	public static void main(final String[] args) {
		Options options = buildOptions();
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine line = parser.parse(options, args);
			if (line.hasOption(HELP)) {
				printHelp(options);
			} else {
				Game _game = new Game();
				start(_game, line);
			}
		} catch (ParseException exception) {
			System.err.println("Error: " + exception.getMessage());
			Main.printHelp(options);
			System.exit((-1));
		}
	}

	public static Game start(final Game game, final CommandLine cl) {
		try {
			Game _xblockexpression = null;
			{
				final String[] externalCmds = Main.getOptionsValuesWithoutNull(
						cl, Main.EXTERNAL_AI_PROGRAM);
				String[] workingDirs = Main.getOptionsValuesWithoutNull(cl,
						Main.WORK_DIR_AI_PROGRAM);
				final String[] _converted_workingDirs = (String[]) workingDirs;
				boolean _isEmpty = ((List<String>) Conversions
						.doWrapArray(_converted_workingDirs)).isEmpty();
				if (_isEmpty) {
					final Function1<String, String> _function = new Function1<String, String>() {
						@Override
						public String apply(final String it) {
							return null;
						}
					};
					List<String> _map = ListExtensions.<String, String> map(
							((List<String>) Conversions
									.doWrapArray(externalCmds)), _function);
					workingDirs = ((String[]) Conversions.unwrapArray(_map,
							String.class));
				}
				int _length = externalCmds.length;
				int _length_1 = workingDirs.length;
				boolean _notEquals = (_length != _length_1);
				if (_notEquals) {
					throw new ParseException(
							"The numbers of arguments of -a and -w must be equal.");
				}
				final IntegerRange indices = new IntegerRange(0, 3);
				int _length_2 = externalCmds.length;
				Iterable<Integer> _drop = IterableExtensions.<Integer> drop(
						indices, _length_2);
				final Function1<Integer, String> _function_1 = new Function1<Integer, String>() {
					@Override
					public String apply(final Integer it) {
						return Main.DEFAULT_COMMAND;
					}
				};
				Iterable<String> _map_1 = IterableExtensions
						.<Integer, String> map(_drop, _function_1);
				final Iterable<String> cmds = Iterables.<String> concat(
						((Iterable<? extends String>) Conversions
								.doWrapArray(externalCmds)), _map_1);
				final String[] _converted_workingDirs_1 = (String[]) workingDirs;
				final Function1<Integer, String> _function_2 = new Function1<Integer, String>() {
					@Override
					public String apply(final Integer it) {
						return Main.DEFAULT_WORK_DIR;
					}
				};
				Iterable<String> _map_2 = IterableExtensions
						.<Integer, String> map(indices, _function_2);
				Iterable<String> _plus = Iterables
						.<String> concat(
								((Iterable<? extends String>) Conversions
										.doWrapArray(_converted_workingDirs_1)),
								_map_2);
				final Iterator<String> workingDirsItr = _plus.iterator();
				final ArrayList<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>> ais = Lists
						.<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>> newArrayList();
				final Procedure2<String, Integer> _function_3 = new Procedure2<String, Integer>() {
					@Override
					public void apply(final String cmd, final Integer index) {
						try {
							String[] _split = cmd.split(" ");
							String _next = workingDirsItr.next();
							final ExternalComputerPlayer com = new ExternalComputerPlayer(
									_split, _next);
							AIInitializer _aIInitializer = new AIInitializer(
									com, (index).intValue());
							Manipulator<Game, String[]> _limittingSumTime = _aIInitializer
									.limittingSumTime(1, 5000);
							AIManipulator _aIManipulator = new AIManipulator(
									com, (index).intValue());
							Manipulator<Game, String[]> _limittingSumTime_1 = _aIManipulator
									.limittingSumTime(1, 1000);
							Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>> _mappedTo = Pair
									.<Manipulator<Game, String[]>, Manipulator<Game, String[]>> of(
											_limittingSumTime,
											_limittingSumTime_1);
							ais.add(_mappedTo);
						} catch (final Throwable _t) {
							if (_t instanceof IOException) {
								final IOException e = (IOException) _t;
								System.exit((-1));
							} else {
								throw Exceptions.sneakyThrow(_t);
							}
						}
					}
				};
				IterableExtensions.<String> forEach(cmds, _function_3);
				int tmpLogLevel = 2;
				boolean _hasOption = cl.hasOption(Main.LOG_LEVEL);
				if (_hasOption) {
					try {
						String _optionValue = cl.getOptionValue(Main.LOG_LEVEL,
								"2");
						int _parseInt = Integer.parseInt(_optionValue);
						tmpLogLevel = _parseInt;
					} catch (final Throwable _t) {
						if (_t instanceof Exception) {
							final Exception e = (Exception) _t;
						} else {
							throw Exceptions.sneakyThrow(_t);
						}
					}
				}
				final int logLevel = tmpLogLevel;
				final boolean silent = cl.hasOption(Main.SILENT);
				Logger _instance = Logger.getInstance();
				_instance.initialize(logLevel, silent);
				Main.playGame(game, ais);
				Logger _instance_1 = Logger.getInstance();
				_instance_1.finalize();
				_xblockexpression = game;
			}
			return _xblockexpression;
		} catch (Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	public static Game playGame(
			final Game game,
			final List<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>> ais) {
		Game _xblockexpression = null;
		{
			game.initialize();
			final Consumer<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>> _function = new Consumer<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>>() {
				@Override
				public void accept(
						final Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>> it) {
					Manipulator<Game, String[]> _key = it.getKey();
					_key.run(game);
				}
			};
			ais.forEach(_function);
			while ((!game.isFinished())) {
				{
					boolean _isInitialState = game.isInitialState();
					if (_isInitialState) {
						Logger _instance = Logger.getInstance();
						_instance.outputLog("", Logger.LOG_LEVEL_DETAILS);
					} else {
						Logger _instance_1 = Logger.getInstance();
						_instance_1.outputLog("", Logger.LOG_LEVEL_STATUS);
					}
					Logger _instance_2 = Logger.getInstance();
					int _turn = game.getTurn();
					String _plus = ("Turn " + Integer.valueOf(_turn));
					_instance_2.outputLog(_plus, Logger.LOG_LEVEL_STATUS);
					final ArrayList<List<String>> commands = Lists
							.<List<String>> newArrayList();
					final Consumer<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>> _function_1 = new Consumer<Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>>>() {
						@Override
						public void accept(
								final Pair<Manipulator<Game, String[]>, Manipulator<Game, String[]>> it) {
							Manipulator<Game, String[]> _value = it.getValue();
							String[] _run = _value.run(game);
							List<String> _list = IterableExtensions
									.<String> toList(((Iterable<String>) Conversions
											.doWrapArray(_run)));
							commands.add(_list);
						}
					};
					ais.forEach(_function_1);
					game.processTurn(commands);
					Logger _instance_3 = Logger.getInstance();
					_instance_3.outputLog("Turn finished. Game status:",
							Logger.LOG_LEVEL_DETAILS);
					Logger _instance_4 = Logger.getInstance();
					String _status = game.getStatus();
					_instance_4.outputLog(_status, Logger.LOG_LEVEL_STATUS);
				}
			}
			Logger _instance = Logger.getInstance();
			_instance.outputLog("", Logger.LOG_LEVEL_STATUS);
			Logger _instance_1 = Logger.getInstance();
			_instance_1.outputLog("Game Finished", Logger.LOG_LEVEL_STATUS);
			Logger _instance_2 = Logger.getInstance();
			String _winner = game.getWinner();
			String _plus = ("Winner: " + _winner);
			_instance_2.outputLog(_plus, Logger.LOG_LEVEL_RESULT);
			_xblockexpression = game;
		}
		return _xblockexpression;
	}

	public static String[] getOptionsValuesWithoutNull(final CommandLine cl,
			final String option) {
		String[] _xifexpression = null;
		boolean _hasOption = cl.hasOption(option);
		if (_hasOption) {
			_xifexpression = cl.getOptionValues(option);
		} else {
			_xifexpression = new String[] {};
		}
		return _xifexpression;
	}
}
