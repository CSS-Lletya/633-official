package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.cooking.PieCreation;

@InventoryWrapper(itemId = {1931, 1946, 1953, 2313, 1982, 2283, 2285, 1985, 1951,2315,2140, 6032, 1929, 7164,434,7166,1955,1982,1957,7172,
		1965,7174,333, 7182, 1942, 7184, 329, 361, 7192, 1942, 7194, 2136, 2876, 7202, 3226, 7204, 5504, 5982, 7212, 1955, 7214}, itemNames = {  })
public class PieCreationItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		PieCreation.create(player, firstItem, secondItem);
	}
}