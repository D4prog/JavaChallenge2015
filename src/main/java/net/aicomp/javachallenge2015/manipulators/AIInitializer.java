package net.aicomp.javachallenge2015.manipulators;

import java.util.ArrayList;
import java.util.List;

import net.aicomp.javachallenge2015.Game;
import net.aicomp.javachallenge2015.log.Logger;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

public class AIInitializer extends GameManipulator {
	private static final String READY = "READY";
	private List<String> _lines;

	public AIInitializer(ExternalComputerPlayer com, int index) {
		super(com, index);
	}

	@Override
	protected void runPreProcessing(Game game) {
		_lines = new ArrayList<String>();
	}

	@Override
	protected void sendDataToAI(Game game) {
	}

	@Override
	protected void receiveDataFromAI() {
		String line = "";
		do {
			line = _com.readLine();
			if (line != null) {
				line = line.trim();
				_lines.add(line);
			}
		} while (!READY.equals(line));
	}

	@Override
	protected String[] runPostProcessing() {
		if (_com.available() && !_com.getErrorLog().isEmpty()) {
			Logger.outputLog("AI" + _index + ">>STDERR: " + _com.getErrorLog(), Logger.LOG_LEVEL_DETAILS);
		}
		String[] ret = new String[_lines.size()];
		for (String line : _lines) {
			Logger.outputLog("AI" + _index + ">>STDOUT: " + line, Logger.LOG_LEVEL_DETAILS);
			ret[_lines.indexOf(line)] = line;
		}
		return ret;
	}
}
