package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {1975, 1927}, itemNames = {  })
public class ChocolateMilkItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(1975), new Item(1927)).execute(firstItem, secondItem, () -> {
			if (player.getInventory().addItem(new Item(1977)))
				player.getInventory().replaceItems(new Item(1927), new Item(1925));
		});
	}
}