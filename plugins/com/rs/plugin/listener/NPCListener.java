package com.rs.plugin.listener;

import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

/**
 * 
 * @author Dennis
 *
 */
public abstract class NPCListener {
	
	/**
	 * The functionality to be executed as soon as this execution is called.
	 * @param player the player we are executing this command for.
	 * @param cmd the command that we are executing for this player.
	 */
	public void execute(Player player, NPC npc, int option) {
	}
	
	public void executeItemOnNPC(Player player, NPC npc, Item itemUsed) {
	}
}
