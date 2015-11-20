package net.aicomp.javachallenge2015.commands;

public class CommandBuilder {
	public static final String UP = "U";
	public static final String DOWN = "D";
	public static final String RIGHT = "R";
	public static final String LEFT = "L";
	public static final String ATTACK = "A";
	public static final String NONE = "N";

	private CommandBuilder() {
		// do nothing
	}

	public static ICommand createCommand(String input) {
		if (input.equals(UP)) {
			return new Up();
		}
		if (input.equals(DOWN)) {
			return new Down();
		}
		if (input.equals(LEFT)) {
			return new Left();
		}
		if (input.equals(RIGHT)) {
			return new Right();
		}
		if (input.equals(ATTACK)) {
			return new Attack();
		}
		if (input.equals(NONE)) {
			return new None();
		}

		return new Invalid();
	}
}
