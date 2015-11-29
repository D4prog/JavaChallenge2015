package net.aicomp.javachallenge2015.commands;

public class CommandBuilder {
	private static final String UP = "U";
	private static final String DOWN = "D";
	private static final String RIGHT = "R";
	private static final String LEFT = "L";
	private static final String ATTACK = "A";

	private CommandBuilder() {
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
		return new None();
	}
}
