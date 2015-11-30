package net.aicomp.javachallenge2015.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Logger {
	private static PrintWriter _writer;
	private static LogObject logObject;
	private static ObjectMapper mapper = new ObjectMapper();

	private Logger() {
	}

	public static void initialize() {
		File file = new File("./log.txt");
		logObject = new LogObject();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			_writer = new PrintWriter(file.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		if (_writer != null) {
			_writer.close();
		}
	}

	public static void outputLog(String message) {
		_writer.println(message.trim());
		_writer.flush();
	}

	public static void outputInitialState(String[][] initialState) {
		logObject.getReplay().addInitialState(initialState);
	}

	public static void outputCommand(String command) {
		logObject.getReplay().addCommand(command);
	}

	public static void outputLogObject(int winnerId) {
		logObject.setWinner(winnerId);
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(logObject);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
