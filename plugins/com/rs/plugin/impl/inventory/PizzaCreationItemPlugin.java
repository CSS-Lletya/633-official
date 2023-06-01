package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.cooking.PizzaTopping;

@InventoryWrapper(itemId = {2142,2140,319,2116,2118}, itemNames = {  })
public class PizzaCreationItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem) {
		PizzaTopping.add(player, firstItem, secondItem);
	}
}