package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.herblore.TarCreation;

@InventoryWrapper(itemId = { 1939, 233, 249, 251, 253, 255, 1973}, itemNames = {})
public class PestleAndMortarItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if (TarCreation.produce(player, firstItem, secondItem))
			return;
		new UseWith(new Item(233), new Item(1973)).execute(firstItem, secondItem, () -> {
			player.getInventory().replaceItems(new Item(1973), new Item(1975));
		});
	}
}