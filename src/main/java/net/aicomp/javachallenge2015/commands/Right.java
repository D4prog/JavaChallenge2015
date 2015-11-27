package net.aicomp.javachallenge2015.commands;

import net.aicomp.javachallenge2015.Field;
import net.aicomp.javachallenge2015.Player;
import net.exkazuu.gameaiarena.api.Direction4;

public class Right implements ICommand {
	@Override
	public void doCommand(Player player, Field field, Player[] players) {
		player.move(Direction4.RIGHT, field, players);
	}

	@Override
	public String getValue() {
		return "R";
	}
}
