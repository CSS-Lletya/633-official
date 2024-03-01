package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.firemaking.LightSource;

@InventoryWrapper(itemId = { 590, 596, 36, 38, 4529, 4522, 4537, 7051, 4548, 5014, 4701,  594, 33, 32, 4534, 4524, 4539, 7053, 4550, 5013, 4702 }, itemNames = {  })
public class LightSourceItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (LightSource.lightSource(player, slotId) || LightSource.extinguishSource(player, slotId, false))
			return;
	}
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if (LightSource.lightSource(player, toSlot) || LightSource.extinguishSource(player, toSlot, false))
			return;
	}
}