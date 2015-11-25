package net.aicomp.javachallenge2015.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Logger {
	public static final int LOG_LEVEL_RESULT = 0;
	public static final int LOG_LEVEL_STATUS = 1;
	public static final int LOG_LEVEL_DETAILS = 2;

	private static PrintWriter _writer;
	private static int _logLevel;
	private static LogObject logObject = new LogObject();
	private static ObjectMapper mapper = new ObjectMapper();

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

	public static void close() {
		if (_writer != null) {
			_writer.close();
		}
	}

	public static void outputLog(String message, int targetLogLevel) {
		if (_logLevel >= targetLogLevel) {
			_writer.println(message.trim());
			logObject.addReplay(message);
		}
	}

	public static void createMessage(int playerId, String input) {
		logObject.addMessage(playerId, input);
	}

	public static void outputLogObject(int winnerId) {
		logObject.setWinner(winnerId);
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(logObject);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
