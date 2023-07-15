package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.traveling.TeleportTabs;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = { 8007, 8008, 8009, 8010, 8011, 8012, 8013, 18809, 18810, 18811, 18812, 18813, 18814, 20175,
		13598, 13599, 13600, 13601, 13602, 13603, 13604, 13605, 13606, 13607, 13608, 13609, 13610,
		13611}, itemNames = {})
public class TeleportTabletsItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		TeleportTabs.useTeleTab(player, item.getId());
	}
}