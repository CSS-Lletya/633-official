package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.Skills;

/**
 * Extra information regarding charges, such.. : https://runescape.fandom.com/wiki/Seaweed_net
 * @author Dennis
 *
 */
@InventoryWrapper(itemId = {14858, 1794}, itemNames = {})
public class SeaweedNetItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(1794), new Item(14858)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(1794));
			player.getInventory().replaceItems(new Item(14858), new Item(14859));
			player.getSkills().addExperience(Skills.CRAFTING, 83);
		});
	}
}