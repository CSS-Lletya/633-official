package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.AmuletStringing;

@InventoryWrapper(itemId = { 1759, 1673, 1675, 1677, 1679, 1681, 1683, 6579}, itemNames = {})
public class AmuletStringingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		AmuletStringing.create(player, firstItem, secondItem);
	}
}