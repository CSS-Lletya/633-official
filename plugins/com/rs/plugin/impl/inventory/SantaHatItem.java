package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryType;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {1050})
public class SantaHatItem implements InventoryType {

	@Override
	public void execute(Player player, Item item, int option) throws Exception {
		System.out.println(option);
	}
}