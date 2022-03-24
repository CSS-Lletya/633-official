package com.rs.plugin.listener;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;

/**
 * 
 * @author Dennis
 *
 */
public interface ObjectType {
	
	/**
	 * The functionality to be executed as soon as this command is called.
	 * @param player the player we are executing this command for.
	 * @param cmd the command that we are executing for this player.
	 */
	void execute(Player player, GameObject object, int optionId) throws Exception;
}
