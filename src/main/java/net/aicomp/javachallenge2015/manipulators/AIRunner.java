package net.aicomp.javachallenge2015.manipulators;

import net.aicomp.javachallenge2015.Game;
import net.aicomp.javachallenge2015.log.Logger;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

public class AIRunner extends GameManipulator {
	private String _line;

	public AIRunner(ExternalComputerPlayer com, int index) {
		super(com, index);
	}

	@Override
	protected void runPreProcessing(Game game) {
		Logger.outputLog("AI" + _index + ">>Writing to stdin, waiting for stdout");

		_line = "";
	}

	@Override
	protected void sendDataToAI(Game game) {
		if (!released()) {
			String input = game.getTurnInformation(_index);
			_com.writeLine(input);
		}
	}

	@Override
	protected void receiveDataFromAI() {
		if (!released()) {
			Logger.outputLog("AI" + _index + ">>receiveDataFromAI");
			_line = _com.readLine();
		}
	}

	@Override
	protected String[] runPostProcessing() {
		if (_com.available() && !_com.getErrorLog().isEmpty()) {
			Logger.outputLog("AI" + _index + ">>STDERR: " + _com.getErrorLog());
		}
		Logger.outputLog("AI" + _index + ">>STDOUT: " + _line);
		String[] ret = null;
		if (!(_line == null || _line.isEmpty())) {
			ret = _line.trim().split(" ");
		}
		return ret;
	}
}
