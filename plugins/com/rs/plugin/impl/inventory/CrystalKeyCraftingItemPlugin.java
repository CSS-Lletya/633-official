package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {985,987}, itemNames = {  })
public class CrystalKeyCraftingItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(985), new Item(987)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(985));
			player.getInventory().replaceItems(new Item(987), new Item(989));
		});
	}
}