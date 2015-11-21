package net.aicomp.javachallenge2015.commands;

import net.aicomp.javachallenge2015.Field;
import net.aicomp.javachallenge2015.Player;

public interface ICommand {
	public void doCommand(Player player, Field field);

	public String getValue();
}
