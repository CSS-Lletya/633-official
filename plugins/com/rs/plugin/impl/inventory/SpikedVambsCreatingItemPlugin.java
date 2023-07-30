package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.SpikedVambsCrafting;

@InventoryWrapper(itemId = { 1063, 1065, 2487, 2489, 2491, 10113 }, itemNames = {})
public class SpikedVambsCreatingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		SpikedVambsCrafting.create(player, firstItem, secondItem);
	}
}