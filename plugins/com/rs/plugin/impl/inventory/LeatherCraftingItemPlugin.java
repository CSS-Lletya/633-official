package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = { ItemNames.NEEDLE_1733, ItemNames.THREAD_1734, ItemNames.LEATHER_1741, ItemNames.HARD_LEATHER_1743}, itemNames = {})
public class LeatherCraftingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if (!player.getInventory().containsAny(ItemNames.THREAD_1734)) {
			player.getPackets().sendGameMessage("You need thread to make this.");
			return;
		}
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(ItemNames.LEATHER_1741)).execute(firstItem, secondItem, () -> {
			player.getInterfaceManager().sendInterface(154);
		});
		//TODO: chatbox dialogue for other leathers
	}
}