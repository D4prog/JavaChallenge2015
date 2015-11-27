package net.aicomp.javachallenge2015;

import java.util.Random;
import java.util.Set;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final int INITIAL_LIFE = 1;
	private static final int DEFAULT_REBIRTH_TIME = 5;
	private static final int DEFAULT_INVINCIBLE_TIME = 5;
	private static final int ATTACK_WAIT_TIME = 3;
	private static final int COLLISION_DISTANCE = 7;
	private static final Point2 FALLEN_POINT = new Point2(-1, -1);

	private int life;
	private Point2 point;
	private int rebirthTime = 0;
	private int waitTime = 0;
	private int invincibleTime = 0;
	private Random rnd;

	private Direction4 dir;

	private ICommand command;

	public Player(Random random) {
		this.life = INITIAL_LIFE;
		rnd = random;
	}

	public void initialize(Field field, Player[] players) {
		spawn(field, players);
		setRandomDir();
		command = CommandBuilder.createCommand("N");
	}

	private void spawn(Field field, Player[] players) {
		Point2[] points = field.getSpawnablePoints(players).toArray(new Point2[0]);
		point = points[rnd.nextInt(points.length)];
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

	public boolean isThere(Set<Point2> area) {
		if (isAlive() && rebirthTime > 0) {
			return false;
		}
		return area.contains(point);
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
		life--;
		rebirthTime = DEFAULT_REBIRTH_TIME;
		point = FALLEN_POINT;
	}

	public void refresh(Field field, Player[] players) {
		if (invincibleTime > 0) {
			invincibleTime--;
		}
		if (rebirthTime > 0) {
			rebirthTime--;
			if (rebirthTime == 0) {
				invincibleTime = DEFAULT_INVINCIBLE_TIME;
				spawn(field, players);
			}
		}
		if (waitTime > 0) {
			waitTime--;
		}
	}

	public boolean isInvincible() {
		return invincibleTime > 0;
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
