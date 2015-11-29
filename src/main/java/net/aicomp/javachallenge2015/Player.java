package net.aicomp.javachallenge2015;

import java.util.Random;
import java.util.Set;

import net.aicomp.javachallenge2015.commands.CommandBuilder;
import net.aicomp.javachallenge2015.commands.ICommand;
import net.exkazuu.gameaiarena.api.Direction4;
import net.exkazuu.gameaiarena.api.Point2;

public class Player {
	private static final Point2 FALLEN_POINT = new Point2(-1, -1);

	private Point2 _location;
	private Direction4 _direction;
	private ICommand _command;
	private int _waitTime;

	public Player(Game game, Field field, Player[] players) {
		Point2[] points = field.getSpawnablePoints(players).toArray(new Point2[0]);
		Random random = game.getRandom();
		_location = points[random.nextInt(points.length)];
		_direction = Direction4.values()[random.nextInt(Direction4.values().length)];
		_command = CommandBuilder.createCommand("N");
	}

	public boolean isAlive() {
		return _location.x != -1 || _location.y != -1;
	}

	public boolean isThere(Set<Point2> area) {
		return area.contains(_location);
	}

	public String getCommandValue() {
		return _command.getValue();
	}

	public void setCommand(String commandStr) {
		_command = CommandBuilder.createCommand(isMovable() ? commandStr : "N");
	}

	private boolean isMovable() {
		return _waitTime == 0;
	}

	public void doCommand(Field field, Player[] players) {
		_command.doCommand(this, field, players);
	}

	public void move(Direction4 direction, Field field, Player[] players) {
		Point2 to = direction.move(_location);
		if (field.canMove(to) && !isCollideOtherPlayers(to, players)) {
			_location = to;
		}
		_direction = direction;
	}

	private boolean isCollideOtherPlayers(Point2 point, Player[] players) {
		for (Player player : players) {
			if (!player.isAlive()) {
				continue;
			}
			if (player != this && point.getManhattanDistance(player._location) <= Constants.COLLISION_DISTANCE) {
				return true;
			}
		}
		return false;
	}

	public void attack(Field field) {
		_waitTime = Constants.ATTACK_WAIT_TIME;
		field.dropBlocks(_location.x, _location.y, _direction);
	}

	public void fall() {
		_location = FALLEN_POINT;
	}

	public void refresh() {
		if (_waitTime > 0) {
			_waitTime--;
		}
	}

	public boolean isCollided(Point2 point) {
		if (this._location == null) {
			return false;
		}
		return point.getManhattanDistance(this._location) <= Constants.COLLISION_DISTANCE;
	}

	public StringBuilder serializeOnlyLocation(StringBuilder builder) {
		builder.append(_location.x);
		builder.append(" ");
		builder.append(_location.y);
		builder.append(" ");
		builder.append(_direction.name().charAt(0));
		return builder;
	}

	public StringBuilder serialize(StringBuilder builder) {
		serializeOnlyLocation(builder);
		builder.append(" ");
		builder.append(_waitTime);
		return builder;
	}
}
