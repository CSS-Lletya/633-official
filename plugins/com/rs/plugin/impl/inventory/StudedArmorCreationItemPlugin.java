package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.StudedArmorCrafting;

@InventoryWrapper(itemId = { 1129, 1095, 2370 }, itemNames = {})
public class StudedArmorCreationItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(1129), new Item(2370)).execute(firstItem, secondItem, () -> {
			new StudedArmorCrafting(player, new Item(1129)).start();
		});
		new UseWith(new Item(1095), new Item(2370)).execute(firstItem, secondItem, () -> {
			new StudedArmorCrafting(player, new Item(1095)).start();
		});
	}
}