package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {ItemNames.NEWCOMER_MAP_550}, itemNames = {  })
public class NewcomerMapItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		player.getInterfaceManager().sendInterface(270);
	}
}