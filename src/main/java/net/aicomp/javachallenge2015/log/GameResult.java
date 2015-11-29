package net.aicomp.javachallenge2015.log;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;

public class GameResult {
	private List<Object> _log;
	private int _winnerId;
	private StringBuilder _buffer;

	public GameResult() {
		_log = new ArrayList<Object>();
		_winnerId = -1;
		_buffer = new StringBuilder();
	}

	public List<Object> getLog() {
		return _log;
	}

	public int getWinner() {
		return _winnerId;
	}

	public List<String> getReplay() {
		return Lists.newArrayList(_buffer.toString());
	}

	public void addReplay(String content) {
		_buffer.append(content);
		_buffer.append('\n');	// must be '\n' due to the specification of the replayer
	}

	public void writeAsJson(String lastReplay, int winnerId) {
		addReplay(lastReplay);
		_winnerId = winnerId;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(this);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
