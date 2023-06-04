package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.cooking.ChocolateCake;

@InventoryWrapper(itemId = {1973, 1891}, itemNames = {  })
public class ChocolateCakeItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem) {
		new ChocolateCake(player).start();
	}
}