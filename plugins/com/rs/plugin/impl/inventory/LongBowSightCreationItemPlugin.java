package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.Skills;

@InventoryWrapper(itemId = {18330, ItemNames.MAGIC_LONGBOW_859, ItemNames.MAPLE_LONGBOW_851, 18332, 18331}, itemNames = {})
public class LongBowSightCreationItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		if (player.getInventory().getFreeSlots() < 2) {
			player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
			return;
		}
		if (item.getId() == 18332 || item.getId() == 18331 && option == 3) {
			player.getInventory().replaceItems(item, new Item(18330));
			player.getInventory().addItem(new Item((item.getId() == 18332 ? 859 : 851)));
		}
	}
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if (player.getSkills().getTrueLevel(Skills.DUNGEONEERING) < 45 || player.getSkills().getTrueLevel(Skills.RANGE) < 45) {
			player.getPackets().sendGameMessage("You need a dungeoneering and ranged level of at least 45 to do this.");
			return;
		}
		new UseWith(new Item(18330), new Item(ItemNames.MAGIC_LONGBOW_859)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(18330));
			player.getInventory().replaceItems(new Item(ItemNames.MAGIC_LONGBOW_859), new Item(18332));
		});
		new UseWith(new Item(18330), new Item(ItemNames.MAPLE_LONGBOW_851)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(18330));
			player.getInventory().replaceItems(new Item(ItemNames.MAPLE_LONGBOW_851), new Item(18331));
		});
	}
}