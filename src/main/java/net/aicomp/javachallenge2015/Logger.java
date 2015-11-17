package net.aicomp.javachallenge2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class Logger {
	public static final int LOG_LEVEL_RESULT = 0;
	public static final int LOG_LEVEL_STATUS = 1;
	public static final int LOG_LEVEL_DETAILS = 2;

	private static PrintWriter _writer;
	private static int _logLevel;

	private Logger() {
	}

	public static void initialize(int logLevel) {
		_logLevel = logLevel;

		File file = new File("./log.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			_writer = new PrintWriter(file.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void finalize() {
		if (_writer != null) {
			_writer.close();
		}
	}

	public static void outputLog(String message, int targetLogLevel) {
		if (_logLevel >= targetLogLevel) {
			System.out.println(message.trim());
			_writer.println(message.trim());
		}
	}
}
