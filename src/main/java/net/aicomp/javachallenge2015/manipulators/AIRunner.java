package net.aicomp.javachallenge2015.manipulators;

import net.aicomp.javachallenge2015.Game;
import net.aicomp.javachallenge2015.log.Logger;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

public class AIRunner extends GameManipulator {
	private String _line;
	private String _dataForAI;

	public AIRunner(ExternalComputerPlayer com) {
		super(com);
	}

	@Override
	protected void runPreProcessing(Game game) {
		int index = game.getCurrentPlayerIndex();
		
		String log = game.serializeForLog(new StringBuilder()).toString();
		Logger.get().writeLog(log);
		game.getResult().addReplay(log);
		
		_dataForAI = game.serializeForAI(new StringBuilder()).toString();
		Logger.get().writeLog("AI" + index + " >> STDIN:");
		Logger.get().writeLog(_dataForAI);

		_line = "";
	}

	@Override
	protected void sendDataToAI(Game game) {
		if (!released()) {
			_com.writeLine(_dataForAI);
		}
	}

	@Override
	protected void receiveDataFromAI(Game game) {
		if (!released()) {
			_line = _com.readLine();
		}
	}

	@Override
	protected String runPostProcessing(Game game) {
		int index = game.getCurrentPlayerIndex();
		Logger.get().writeLog("AI" + index + " >> STDOUT: " + _line);
		if (_com.available()) {
			String errorLog = _com.getErrorLog();
			if (!errorLog.isEmpty()) {
				Logger.get().writeLog("AI" + index + " >> STDERR: " + errorLog);
			}
		}
		return _line;
	}
}
