package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.mining.CoalBag;

@InventoryWrapper(itemId = {ItemNames.COAL_BAG_18339, 453}, itemNames = {  })
public class CoalBagItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		CoalBag.interact(player, option);
	}
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(ItemNames.COAL_BAG_18339), new Item(ItemNames.COAL_453)).execute(firstItem, secondItem, () -> {
			CoalBag.addCoal(player, new Item(453));
		});
	}
}