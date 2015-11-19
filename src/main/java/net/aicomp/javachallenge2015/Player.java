package net.aicomp.javachallenge2015;

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

	private int life;
	private Point2 point;
	private int rebirthTime = 0;
	private int waitTime = 0;
	private int invincibleTime = 0;
	private Player[] players;

	private Direction4 dir;

	private ICommand command;

	public Player(Random rnd) {
		this.life = INITIAL_LIFE;
		point = new Point2((int) (rnd.nextDouble() * 40),
				(int) (rnd.nextDouble() * 40));
		setRandomDir(rnd);
		command = CommandBuilder.createCommand("N");
	}

	public void setRandomDir(Random rnd) {
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
		if (isAlive() || rebirthTime > 0) {
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
		return (isOnBoard() ? point.x : -1) + " "
				+ (isOnBoard() ? point.y : -1);
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
	}

	public void refresh() {
		if (invincibleTime > 0) {
			invincibleTime--;
		}
		if (rebirthTime > 0) {
			rebirthTime--;
			if (rebirthTime == 0) {
				invincibleTime = DEFAULT_INVINCIBLE_TIME;
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
