package com.rs.plugin.listener;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * 
 * @author Dennis
 *
 */
public interface InventoryListener {
	
	void execute(Player player, Item item, int option) throws Exception;
}
