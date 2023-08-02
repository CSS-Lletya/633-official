package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.prayer.BlessSpiritShieldCreation;

@InventoryWrapper(itemId = {ItemNames.HOLY_ELIXIR_13754, ItemNames.SPIRIT_SHIELD_13734}, itemNames = {  })
public class BlessSpiritShieldItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(ItemNames.HOLY_ELIXIR_13754), new Item(ItemNames.SPIRIT_SHIELD_13734)).execute(firstItem, secondItem, () -> new BlessSpiritShieldCreation(player).start());
	}
}