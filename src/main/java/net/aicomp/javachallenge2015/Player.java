package net.aicomp.javachallenge2015;

import java.util.Random;
import java.util.Set;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final int ATTACK_WAIT_TIME = 3;
	private static final int COLLISION_DISTANCE = 7;
	private static final Point2 FALLEN_POINT = new Point2(-1, -1);

	private Point2 point;
	private int waitTime = 0;
	private Random rnd;

	private Direction4 dir;

	private ICommand command;

	public Player(Random random) {
		rnd = random;
	}

	public void initialize(Field field, Player[] players) {
		Point2[] points = field.getSpawnablePoints(players).toArray(new Point2[0]);
		point = points[rnd.nextInt(points.length)];
		dir = Direction4.values()[rnd.nextInt(Direction4.values().length)];
		command = CommandBuilder.createCommand("N");
	}

	public boolean isAlive() {
		return point.x == -1 && point.y == -1;
	}

	public boolean isThere(Set<Point2> area) {
		return area.contains(point);
	}

	public String getPlaceAndDirection() {
		return point.x + " " + point.y + " " + dir.name().substring(0, 1);
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
		Point2 tpoint = direction.move(point);
		if (field.canMove(tpoint) && !isCollideOtherPlayers(tpoint, players)) {
			point = tpoint;
		}
		dir = direction;
	}

	private boolean isCollideOtherPlayers(Point2 point, Player[] players) {
		for (Player player : players) {
			if (!player.isAlive()) {
				continue;
			}
			if (player != this && point.getManhattanDistance(player.point) <= COLLISION_DISTANCE) {
				return true;
			}
		}
		return false;
	}

	public void attack(Field field) {
		waitTime = ATTACK_WAIT_TIME;
		field.setLimit(point.x, point.y, dir);
	}

	public void fall() {
		point = FALLEN_POINT;
	}

	public void refresh() {
		if (waitTime > 0) {
			waitTime--;
		}
	}

	public boolean isCollided(Point2 point) {
		if (this.point == null) {
			return false;
		}
		return point.getManhattanDistance(this.point) <= COLLISION_DISTANCE;
	}

	public String getStatus() {
		return getPlaceAndDirection() + " " + waitTime;
	}
}
