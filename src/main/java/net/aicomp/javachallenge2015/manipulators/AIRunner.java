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
		Logger.outputLog("AI" + _index + ">>Writing to stdin, waiting for stdout", Logger.LOG_LEVEL_DETAILS);

		String log = game.getLogInformation(_index);
		Logger.outputLog(log, Logger.LOG_LEVEL_STATUS);

		String message = game.getMessageInformation(_index);
		Logger.createMessage(_index, message);

		_line = "";
	}

	@Override
	protected void sendDataToAI(Game game) {
		Logger.outputLog("AI" + _index + ">>sendDataToAI", Logger.LOG_LEVEL_DETAILS);
		String input = game.getTurnInformation(_index);
		_com.writeLine(input);
	}

	@Override
	protected void receiveDataFromAI() {
		Logger.outputLog("AI" + _index + ">>receiveDataFromAI", Logger.LOG_LEVEL_DETAILS);
		_line = _com.readLine();
	}

	@Override
	protected String[] runPostProcessing() {
		if (_com.available() && !_com.getErrorLog().isEmpty()) {
			Logger.outputLog("AI" + _index + ">>STDERR: " + _com.getErrorLog(), Logger.LOG_LEVEL_DETAILS);
		}
		Logger.outputLog("AI" + _index + ">>STDOUT: " + _line, Logger.LOG_LEVEL_DETAILS);
		String[] ret = null;
		if (!(_line == null || _line.isEmpty())) {
			ret = _line.trim().split(" ");
		}
		return ret;
	}
}
