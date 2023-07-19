package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {4251}, itemNames = {  })
public class EctophialItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		Magic.useEctoPhial(player, item);
	}
}