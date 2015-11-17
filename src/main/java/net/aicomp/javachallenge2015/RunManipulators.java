package net.aicomp.javachallenge2015;

import net.exkazuu.gameaiarena.manipulator.Manipulator;

public class RunManipulators {
	private Manipulator<Game, String[]> initialize;
	private Manipulator<Game, String[]> run;

	public RunManipulators(Manipulator<Game, String[]> manipulator,
			Manipulator<Game, String[]> manipulator2) {
		initialize = manipulator;
		run = manipulator2;
	}

	public Manipulator<Game, String[]> getInitializeManipulator() {
		return initialize;
	}

	public Manipulator<Game, String[]> getRunManipulator() {
		return run;
	}
}
