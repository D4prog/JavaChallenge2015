package net.aicomp.javachallenge2015.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Logger {
	private static Logger _instance;
	private PrintWriter _writer;

	public static Logger get() {
		if (_instance == null) {
			_instance = new Logger();
		}
		return _instance;
	}

	private Logger() {
		File file = new File("./log.txt");
		try {
			_writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (_writer != null) {
			_writer.close();
			_writer = null;
		}
	};

	public void writeLog(String content) {
		_writer.println(content.trim());
		_writer.flush();
	}
}
