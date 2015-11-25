package net.aicomp.javachallenge2015.log;

public class Message {
	private final int target;
	private final String message;

	public Message(int t, String m) {
		target = t;
		message = m;
	}

	public int getTarget() {
		return target;
	}

	public String getMessage() {
		return message;
	}

}
