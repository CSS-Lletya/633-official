package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.BattlestaffCrafting;

@InventoryWrapper(itemId = {1391, 569, 571, 573, 575}, itemNames = {})
public class BattlestaffCreationItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		BattlestaffCrafting.create(player, firstItem, secondItem);
	}
}