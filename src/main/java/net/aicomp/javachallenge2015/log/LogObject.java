package net.aicomp.javachallenge2015.log;

import java.util.ArrayList;
import java.util.List;

public class LogObject {
	private List<Message> log;
	private int winner;
	private StringBuilder replay;

	public LogObject() {
		log = new ArrayList<Message>();
		winner = -1;
		replay = new StringBuilder();
	}

	public List<Message> getLog() {
		return log;
	}

	public void addMessage(int playerId, String message) {
		log.add(new Message(playerId, message));
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winnerId) {
		winner = winnerId;
	}

	public String getReplay() {
		return replay.toString();
	}

	public void addReplay(String turnMessage) {
		replay.append(turnMessage + System.getProperty("line.separator"));
	}
}
