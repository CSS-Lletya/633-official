package com.rs.plugin.impl.inventory;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.mining.AxeAssembly;
import skills.mining.AxeAssembly.AxeData;

@InventoryWrapper(itemId = { 466, 480, 482, 484, 486, 488, 490 }, itemNames = {})
public class BrokenAxesItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		Arrays.stream(AxeData.values())
				.filter(axe -> firstItem.getId() == axe.head && secondItem.getId() == 466 || secondItem.getId() == axe.head && firstItem.getId() == 466)
				.forEach(axe -> new AxeAssembly(player, axe).start());

	}
}