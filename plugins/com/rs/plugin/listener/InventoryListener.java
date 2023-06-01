package com.rs.plugin.listener;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * 
 * @author Dennis
 *
 */
public abstract class InventoryListener {
	
	public void execute(Player player, Item item, int slot, int option) {
	}
	
	public void execute(Player player, Item firstItem, Item secondItem) {
	}
}
