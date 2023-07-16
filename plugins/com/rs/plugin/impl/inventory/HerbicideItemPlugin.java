package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = { 19675 }, itemNames = {})
public class HerbicideItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		player.getInterfaceManager().sendInterface(1006);
	}
}