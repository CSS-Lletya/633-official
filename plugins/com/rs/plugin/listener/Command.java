package com.rs.plugin.listener;

import com.rs.game.player.Player;

/**
 * Represents a command which holds functionality for the execution of
 * it.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public interface Command {
	
	/**
	 * The functionality to be executed as soon as this command is called.
	 * @param player the player we are executing this command for.
	 * @param cmd the command that we are executing for this player.
	 */
	void execute(Player player, String[] cmd, String command) throws Exception;
}
