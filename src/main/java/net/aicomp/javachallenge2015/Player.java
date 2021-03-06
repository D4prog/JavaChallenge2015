package net.aicomp.javachallenge2015;

import java.util.Set;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final int ATTACK_WAIT_TIME = 3;
	private static final int COLLISION_DISTANCE = 3;
	private static final Point2 FALLEN_POINT = new Point2(-1, -1);

	private Point2 location;
	private Direction4 dir;
	private ICommand command;
	private int waitTime = 0;

	public Player(Point2 initialLocation, Direction4 initialDirection) {
		location = initialLocation;
		dir = initialDirection;
		command = CommandBuilder.createCommand("N");
	}

	public boolean isAlive() {
		return location.x != -1 || location.y != -1;
	}

	public boolean isThere(Set<Point2> area) {
		return area.contains(location);
	}

	public String getPlaceAndDirection() {
		return location.x + " " + location.y + " " + dir.name().substring(0, 1);
	}

	public String getCommandValue() {
		return command.getValue();
	}

	public void setCommand(String commandStr) {
		command = CommandBuilder.createCommand(isMovable() ? commandStr : "N");
	}

	private boolean isMovable() {
		return waitTime == 0;
	}

	public void doCommand(Field field, Player[] players) {
		command.doCommand(this, field, players);
	}

	public void move(Direction4 direction, Field field, Player[] players) {
		Point2 to = direction.move(location);
		if (field.canMove(to) && !isCollideOtherPlayers(to, players)) {
			location = to;
		}
		dir = direction;
	}

	private boolean isCollideOtherPlayers(Point2 point, Player[] players) {
		for (Player player : players) {
			if (!player.isAlive()) {
				continue;
			}
			if (player != this && point.getManhattanDistance(player.location) <= COLLISION_DISTANCE) {
				return true;
			}
		}
		return false;
	}

	public void attack(Field field) {
		waitTime = ATTACK_WAIT_TIME;
		field.setLimit(location.x, location.y, dir);
	}

	public void fall() {
		location = FALLEN_POINT;
	}

	public void refresh() {
		if (waitTime > 0) {
			waitTime--;
		}
	}

	public boolean isCollided(Point2 point) {
		if (this.location == null) {
			return false;
		}
		return point.getManhattanDistance(this.location) <= COLLISION_DISTANCE;
	}

	public String getStatus() {
		return getPlaceAndDirection() + " " + waitTime;
	}
}
