package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.GemCutting;

@InventoryWrapper(itemId = { 1755, 1625, 1627, 1629, 1623, 1621, 1619, 1617, 1631, 6571, 1609, 1611, 411, 413, 1613,
		1607, 1605, 1603, 1601, 1615, 6573, 9953, 10107}, itemNames = {})
public class GemCuttingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		GemCutting.cut(player, firstItem, secondItem);
	}
}