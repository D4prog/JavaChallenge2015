package net.aicomp.javachallenge2015;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final int INITIAL_LIFE = 5;
	private static final int DEFAULT_REBIRTH_TIME = 5;
	private static final int DEFAULT_INVINCIBLE_TIME = 5;
	private static final int ATTACK_WAIT_TIME = 2;
	private static final int COLLISION_DISTANCE = 7;
	private static final Point2 FALLEN_POINT = new Point2(-1, -1);

	private int life;
	private Point2 point;
	private int rebirthTime = 0;
	private int waitTime = 0;
	private int invincibleTime = 0;
	private Player[] players;
	private Random rnd;

	private Direction4 dir;

	private ICommand command;

	public Player(Random random, Player[] players) {
		this.life = INITIAL_LIFE;
		rnd = random;
		this.players = players;
		spawn();
		setRandomDir();
		command = CommandBuilder.createCommand("N");
	}

	private void spawn() {
		List<Point2> points = Field.getAllPoints();
		List<Point2> collisionPoints = new ArrayList<Point2>();
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				continue;
			}
			for (Point2 point2 : points) {
				if (players[i].point.getManhattanDistance(point2) <= COLLISION_DISTANCE) {
					collisionPoints.add(point2);
				}
			}
		}
		points.removeAll(collisionPoints);
		point = points.get(rnd.nextInt(points.size()));
	}

	public void setRandomDir() {
		int d = rnd.nextInt(Direction4.values().length);
		dir = Direction4.values()[d];
	}

	public int getLife() {
		return life;
	}

	public boolean isAlive() {
		return life > 0;
	}

	public boolean isThere(int sx, int sy, int gx, int gy) {
		if (isAlive() && rebirthTime > 0) {
			return false;
		}
		List<Point2> area = Point2.getPoints(sx, sy, gx, gy);
		for (Point2 point : area) {
			if (this.point.equals(point)) {
				return true;
			}
		}
		return false;
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
		command = CommandBuilder.createCommand(isMovable() ? commandStr : "N");
	}

	private boolean isOnBoard() {
		return rebirthTime == 0;
	}

	private boolean isMovable() {
		return isOnBoard() && waitTime == 0;
	}

	public void doCommand(Field field, Player[] players) {
		this.players = players;
		command.doCommand(this, field);
	}

	public void move(Direction4 direction, Field field) {
		Point2 tpoint = direction.move(point);
		if (field.canMove(tpoint) && !isCollideOtherPlayers(tpoint)) {
			point = tpoint;
		}
		dir = direction;
	}

	private boolean isCollideOtherPlayers(Point2 point) {
		for (Player player : players) {
			if (!player.isAlive()) {
				continue;
			}
			if (player != this
					&& point.getManhattanDistance(player.point) <= COLLISION_DISTANCE) {
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
		life--;
		rebirthTime = DEFAULT_REBIRTH_TIME;
		point = FALLEN_POINT;
	}

	public void refresh() {
		if (invincibleTime > 0) {
			invincibleTime--;
		}
		if (rebirthTime > 0) {
			rebirthTime--;
			if (rebirthTime == 0) {
				invincibleTime = DEFAULT_INVINCIBLE_TIME;
				spawn();
			}
		}
		if (waitTime > 0) {
			waitTime--;
		}
	}

	public boolean isInvincible() {
		return invincibleTime > 0;
	}
}
