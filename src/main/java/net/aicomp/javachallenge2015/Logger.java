package net.aicomp.javachallenge2015;

import java.io.File;
import java.io.PrintWriter;

class Logger {
	public static final int LOG_LEVEL_RESULT = 0;
	public static final int LOG_LEVEL_STATUS = 1;
	public static final int LOG_LEVEL_DETAILS = 2;

	private static Logger _instance;
	private PrintWriter _writer;
	private int _logLevel;
	private boolean _silent;

	private Logger() {
		// do nothing
	}

	public static Logger getInstance() {
		if (_instance == null) {
			_instance = new Logger();
		}
		return _instance;
	}

	public void initialize(int logLevel, boolean silent) {
		_logLevel = logLevel;
		_silent = silent;

		if (!_silent) {
			File file = new File("./log.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			_writer = new PrintWriter(file.getAbsoluteFile());
		}
	}

	@Override
	public void finalize() {
		if (_writer != null) {
			_writer.close();
		}
	}

	public void outputLog(String message, int targetLogLevel) {
		if (_logLevel >= targetLogLevel) {
			System.out.println(message.trim());
			if (!_silent) {
				_writer.println(message.trim());
			}
		}
	}
}