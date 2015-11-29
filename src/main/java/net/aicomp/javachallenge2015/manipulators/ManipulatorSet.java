package net.aicomp.javachallenge2015.manipulators;

import net.aicomp.javachallenge2015.Game;
import net.exkazuu.gameaiarena.manipulator.PauseUnpauseManipulator;

public class ManipulatorSet {
	private PauseUnpauseManipulator<Game, String> _initializer;
	private PauseUnpauseManipulator<Game, String> _runner;

	public ManipulatorSet(PauseUnpauseManipulator<Game, String> initializer,
			PauseUnpauseManipulator<Game, String> runner) {
		this._initializer = initializer;
		this._runner = runner;
	}

	public PauseUnpauseManipulator<Game, String> getInitializer() {
		return _initializer;
	}

	public PauseUnpauseManipulator<Game, String> getRunner() {
		return _runner;
	}
}