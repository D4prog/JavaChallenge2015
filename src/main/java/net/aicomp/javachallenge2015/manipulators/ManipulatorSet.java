package net.aicomp.javachallenge2015.manipulators;

import net.aicomp.javachallenge2015.Game;
import net.exkazuu.gameaiarena.manipulator.PauseUnpauseManipulator;

public class ManipulatorSet {
	private PauseUnpauseManipulator<Game, String[]> initializer;
	private PauseUnpauseManipulator<Game, String[]> runner;

	public ManipulatorSet(PauseUnpauseManipulator<Game, String[]> initializer,
			PauseUnpauseManipulator<Game, String[]> runner) {
		this.initializer = initializer;
		this.runner = runner;
	}

	public PauseUnpauseManipulator<Game, String[]> getInitializer() {
		return initializer;
	}

	public PauseUnpauseManipulator<Game, String[]> getRunner() {
		return runner;
	}
}