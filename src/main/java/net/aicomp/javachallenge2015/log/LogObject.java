package net.aicomp.javachallenge2015.log;

import java.util.ArrayList;
import java.util.List;

public class LogObject {
	private List<Object> log;
	private int winner;
	private Replay[] replay;

	public LogObject() {
		log = new ArrayList<Object>();
		winner = -1;
		replay = new Replay[] { new Replay() };
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

	public Replay[] getReplay() {
		return replay;
	}

}
