package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.impl.interfaces.ExperienceLampInterfacePlugin;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {19775, 19755, 18702, 11640, 18782}, itemNames = {})
public class ExperienceGivingItemsItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		ExperienceLampInterfacePlugin.setLamp(item.getId());
		player.getInterfaceManager().closeInterfaces();
		player.getInterfaceManager().sendInterface(134);
	}
}