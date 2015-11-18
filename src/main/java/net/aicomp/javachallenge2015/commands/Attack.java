package net.aicomp.javachallenge2015.commands;

import net.aicomp.javachallenge2015.Field;
import net.aicomp.javachallenge2015.Player;

public class Attack implements ICommand {

	@Override
	public void doCommand(Player player, Field field) {
		player.attack(field);
	}

	@Override
	public String getValue() {
		return "A";
	}

}
