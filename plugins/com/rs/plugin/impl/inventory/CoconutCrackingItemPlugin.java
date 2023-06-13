package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {2347, 5974}, itemNames = {  })
public class CoconutCrackingItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(2347), new Item(5974)).execute(firstItem, secondItem, () -> {
			player.getInventory().replaceItems(new Item(5974), new Item(5976));
			player.getPackets().sendGameMessage("You break the coconut open with the hammer.");
		});
	}
}