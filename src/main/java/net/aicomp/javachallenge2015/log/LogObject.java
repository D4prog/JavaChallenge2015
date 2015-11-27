package net.aicomp.javachallenge2015.log;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class LogObject {
	private List<Object> log;
	private int winner;
	private List<String> replay;
	private static StringBuilder buffer;

	public LogObject() {
		log = new ArrayList<Object>();
		winner = -1;
		buffer = new StringBuilder();
	}

	public List<Object> getLog() {
		return log;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winnerId) {
		winner = winnerId;
	}

	public List<String> getReplay() {
		return replay;
	}

	public void addReplay(String turnMessage) {
		buffer.append(turnMessage + System.getProperty("line.separator"));
	}

	public void fillReplay() {
		replay = Lists.newArrayList(buffer.toString());
	}
}
