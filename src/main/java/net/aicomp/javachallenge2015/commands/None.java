package net.aicomp.javachallenge2015.commands;

import net.aicomp.javachallenge2015.Field;
import net.aicomp.javachallenge2015.Player;

public class None implements ICommand {
	@Override
	public void doCommand(Player player, Field field, Player[] players) {
	}

	@Override
	public String getValue() {
		return "N";
	}
}
