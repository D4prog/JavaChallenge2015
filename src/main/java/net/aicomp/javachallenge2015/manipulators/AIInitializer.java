package net.aicomp.javachallenge2015.manipulators;

import java.util.ArrayList;
import java.util.List;

import net.aicomp.javachallenge2015.Game;
import net.aicomp.javachallenge2015.log.Logger;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

public class AIInitializer extends GameManipulator {
	private static final String READY = "READY";
	private final List<String> _lines;

	public AIInitializer(ExternalComputerPlayer com) {
		super(com);
		_lines = new ArrayList<String>();
	}

	@Override
	protected void runPreProcessing(Game game) {
		_lines.clear();
	}

	@Override
	protected void sendDataToAI(Game game) {
	}

	@Override
	protected void receiveDataFromAI(Game game) {
		while (true) {
			String line = _com.readLine();
			if (line == null) {
				release();
			}
			line = line.trim();
			_lines.add(line);
			if (READY.equals(line)) {
				break;
			}
		}
	}

	@Override
	protected String runPostProcessing(Game game) {
		int index = game.getCurrentPlayerIndex();
		for (String line : _lines) {
			Logger.get().writeLog("AI" + index + " >> STDOUT: " + line);
		}
		if (_com.available()) {
			String errorLog = _com.getErrorLog();
			if (!errorLog.isEmpty()) {
				Logger.get().writeLog("AI" + index + " >> STDERR: " + errorLog);
			}
		}
		return "READY";
	}
}
