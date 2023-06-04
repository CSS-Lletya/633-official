package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {2798, 3565, 3576, 19042}, itemNames = {  })
public class ClueScrollPuzzleItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		player.getTreasureTrailsManager().openPuzzle(item.getId());
	}
}