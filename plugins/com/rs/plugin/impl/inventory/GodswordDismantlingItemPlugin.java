package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.GodswordDismantling;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = { 11694, 11695, 11696, 11697, 11698, 11699, 11700 }, itemNames = {})
public class GodswordDismantlingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (option == 3)
			GodswordDismantling.dismantleGodsword(player, item.getId());
	}
}