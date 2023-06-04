package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.cooking.TeaCrafting;

@InventoryWrapper(itemId = { 7738, 7702, 7714, 7726, 7691, 7700,7712, 7724 }, itemNames = {})
public class TeaCraftingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem) {
		TeaCrafting.execute(player, firstItem, secondItem);
	}
}