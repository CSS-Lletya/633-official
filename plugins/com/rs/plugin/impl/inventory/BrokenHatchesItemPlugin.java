package com.rs.plugin.impl.inventory;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.woodcutting.HatchetAssembly;
import skills.woodcutting.HatchetAssembly.AxeData;

@InventoryWrapper(itemId = { 508, 510, 512, 514, 516, 518, 6743, 492 }, itemNames = {})
public class BrokenHatchesItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		Arrays.stream(AxeData.values())
				.filter(axe -> firstItem.getId() == axe.head && secondItem.getId() == 492
						|| secondItem.getId() == axe.head && firstItem.getId() == 492)
				.forEach(axe -> new HatchetAssembly(player, axe).start());

	}
}