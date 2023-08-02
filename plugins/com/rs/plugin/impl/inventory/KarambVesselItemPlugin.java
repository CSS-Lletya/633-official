package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {3157, 3159, 3150}, itemNames = {  })
public class KarambVesselItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(3157), new Item(3150)).execute(firstItem, secondItem, () -> {
			int amount = player.getInventory().getNumberOf(3150);
			player.getInventory().deleteItem(new Item(3150, amount));
			player.getDetails().getKarambwanjiStock().getAndIncrement(amount);
			player.getInventory().replaceItems(new Item(3157), new Item(3159));
		});
		new UseWith(new Item(3159), new Item(3150)).execute(firstItem, secondItem, () -> {
			int amount = player.getInventory().getNumberOf(3150);
			player.getInventory().deleteItem(new Item(3150, amount));
			player.getDetails().getKarambwanjiStock().getAndIncrement(amount);
		});
	}
}