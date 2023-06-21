package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.RandomUtils;

@InventoryWrapper(itemId = { ItemNames.OYSTER_407 }, itemNames = {})
public class OysterItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		player.getInventory().replaceItems(new Item(ItemNames.OYSTER_407), (RandomUtils.percentageChance(50) ? 
						  new Item(ItemNames.OYSTER_PEARL_411)
						: RandomUtils.percentageChance(5) ? new Item(ItemNames.OYSTER_PEARLS_413) : new Item(ItemNames.EMPTY_OYSTER_409)));
	}
}