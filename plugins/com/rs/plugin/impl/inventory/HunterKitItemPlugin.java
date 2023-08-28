package com.rs.plugin.impl.inventory;

import java.util.stream.IntStream;

import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = { 11159 }, itemNames = {})
public class HunterKitItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (player.getInventory().getFreeSlots() < 9) {
			player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
			return;
		}
		player.getAudioManager().sendSound(Sounds.HUNTER_KIT_OPENING);
		player.getInventory().deleteItem(item);
		IntStream.of(10150, 10010, 10006, 10031, 10029, 596, 10008, 11260)
		.forEach(player.getInventory()::addItem);
	}
}