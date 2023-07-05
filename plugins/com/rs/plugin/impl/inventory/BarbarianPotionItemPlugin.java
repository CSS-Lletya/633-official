package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.Skills;
import skills.herblore.BarbarianPotion;

@InventoryWrapper(itemId = {
		// caviars
		11326, 11324,
		// potions
		123, 177, 4846, 117, 129, 3012, 135, 3036, 9743, 141, 147, 183, 143, 3020, 10002, 159, 3028, 165, 2456, 171,
		3044, 191,
}, itemNames = {})
public class BarbarianPotionItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		final BarbarianPotion potion = BarbarianPotion.forId(secondItem.getId());
		if (potion == null) {
			return;
		}
		if (!potion.isBoth() && firstItem.getId() == 11324 || secondItem.getId() == 11324) {
			return;
		}
		if (player.getSkills().getLevel(Skills.HERBLORE) < potion.getLevel()) {
			player.getPackets()
					.sendGameMessage("You need a herblore level of " + potion.getLevel() + " to make this mix.");
			return;
		}
		if (potion.getItem() == firstItem.getId() || potion.getItem() == secondItem.getId()) {
			player.getInventory().removeItems(new Item(potion.getItem(), 1));
			player.getInventory().addItem(new Item(potion.getProduct(), 1));
			player.getSkills().addExperience(Skills.HERBLORE, potion.getExp());
			if (potion.isBoth()) {
				player.getInventory().removeItems(new Item(11324, 1));
			}
			player.getInventory().removeItems(new Item(11326, 1));
		}
	}
}