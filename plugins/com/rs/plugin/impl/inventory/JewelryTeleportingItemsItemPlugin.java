package com.rs.plugin.impl.inventory;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {}, itemNames = {
		"ring of duelling",
		"games necklace",
		"amulet of glory (t",
		"amulet of glory",
		"skills necklace",
		"combat bracelet",
		"digsite",
		"ring of wealth",
		"ring of slaying",
		"lumber yard",
		"nardah",
		"tai bwo wannai",
		"bandit camp",
		"phoenix lair",
		"miscellania" })
public class JewelryTeleportingItemsItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (item.getDefinitions().containsOption("Read") && option == 1 || option == 6) {
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(item.getId()).getName() + "_Teleports").addStatistic("Jewelry_Teleports");
		}
	}
}