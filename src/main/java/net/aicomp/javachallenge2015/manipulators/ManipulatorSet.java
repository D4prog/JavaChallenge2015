package net.aicomp.javachallenge2015.manipulators;

import net.aicomp.javachallenge2015.Game;
import net.exkazuu.gameaiarena.manipulator.Manipulator;

public class ManipulatorSet {
	private Manipulator<Game, String[]> initializer;
	private Manipulator<Game, String[]> runner;

	public ManipulatorSet(Manipulator<Game, String[]> initializer, Manipulator<Game, String[]> runner) {
		this.initializer = initializer;
		this.runner = runner;
	}

	public Manipulator<Game, String[]> getInitializeManipulator() {
		return initializer;
	}

	public Manipulator<Game, String[]> getRunManipulator() {
		return runner;
	}
}