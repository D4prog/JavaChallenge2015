package net.aicomp.javachallenge2015.manipulators;

import net.aicomp.javachallenge2015.Game;
import net.exkazuu.gameaiarena.manipulator.RootManipulator;
import net.exkazuu.gameaiarena.player.ExternalComputerPlayer;

abstract class GameManipulator extends RootManipulator<Game, String> {
	protected ExternalComputerPlayer _com;

	public GameManipulator(ExternalComputerPlayer com) {
		_com = com;
	}

	@Override
	public ExternalComputerPlayer getExternalComputerPlayer() {
		return _com;
	}
}
