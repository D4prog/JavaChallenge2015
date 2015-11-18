package net.aicomp.javachallenge2015;

import java.util.Random;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final int INITIAL_LIFE = 5;

	public int life;
	private Point2 point;
	public int rebirthTurn = 0;
	private Direction4 dir;

	private ICommand command;

	public Player(Random rnd) {
		this.life = INITIAL_LIFE;
		point = new Point2((int) (rnd.nextDouble() * 40),
				(int) (rnd.nextDouble() * 40));
		setRandomDir(rnd);
		command = CommandBuilder.createCommand("N");
	}

	/**
	 * 
	 */
	public void setRandomDir(Random rnd) {
		int d = rnd.nextInt(Direction4.values().length);
		dir = Direction4.values()[d];
	}

	public boolean isThere(int x, int y) {
		return life > 0 && point.equals(new Point2(x, y));
	}

	public String getPlace() {
		return point.x + " " + point.y;
	}

	public String getPlaceAndDirection() {
		return getPlace() + " " + dir.name().substring(0, 1);
	}

	public String getCommandValue() {
		return command.getValue();
	}

	public void setCommand(String commandStr) {
		command = CommandBuilder.createCommand(commandStr);
	}

	public void doCommand(Field field) {
		command.doCommand(this, field);
	}

	public void move(Direction4 direction, Field field) {
		Point2 tpoint = direction.move(point);
		if (field.canMove(tpoint)) {
			point = tpoint;
		}
		dir = direction;
	}

	public void attack(Field field) {
		field.setLimit(point.x, point.y, dir);
	}
}
