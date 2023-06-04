package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.cooking.WineCrafting;

@InventoryWrapper(itemId = {1987,1937}, itemNames = {  })
public class WineMixingItemPlugin extends InventoryListener {
	
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem) {
		if (firstItem.getId() == 1937 || secondItem.getId() == 1937 && firstItem.getId() == 1987 || secondItem.getId() == 1987) {
			new WineCrafting(player, firstItem.getId()).start();
		}
	}
}