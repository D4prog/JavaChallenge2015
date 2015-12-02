package net.aicomp.javachallenge2015.log;

import java.util.ArrayList;
import java.util.List;

public class Replay {
	private String[][] initial;
	private List<String> commands;

	public Replay() {
		initial = null;
		commands = new ArrayList<String>();
	}

	public void addInitialState(String[][] initialState) {
		initial = initialState;
	}

	public void addCommand(String command) {
		commands.add(command);
	}

	public String[][] getInitial() {
		return initial;
	}

	public List<String> getCommands() {
		return commands;
	}
}
