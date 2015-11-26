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
		if (UP.equals(input)) {
			return new Up();
		}
		if (DOWN.equals(input)) {
			return new Down();
		}
		if (LEFT.equals(input)) {
			return new Left();
		}
		if (RIGHT.equals(input)) {
			return new Right();
		}
		if (ATTACK.equals(input)) {
			return new Attack();
		}
		if (NONE.equals(input)) {
			return new None();
		}

		return new Invalid();
	}
}
