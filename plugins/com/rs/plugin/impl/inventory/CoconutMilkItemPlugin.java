package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {229, 5976}, itemNames = {  })
public class CoconutMilkItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(229), new Item(5976)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(229));
			player.getInventory().replaceItems(new Item(5974), new Item(5935));
			player.getPackets().sendGameMessage("You pour the coconut milk carefully into the vial.");
		});
	}
}