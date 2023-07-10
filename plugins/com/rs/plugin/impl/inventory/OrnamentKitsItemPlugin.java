package com.rs.plugin.impl.inventory;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import lombok.AllArgsConstructor;

@InventoryWrapper(itemId = { 19333, 19350, 19348, 19348, 19352, 19346, 25312, 19354, 19358, 19356, 19356, 19360, 25314,
		6585, 14479, 4087, 4585, 1187, 11335, 24365, 11335, 14479, 4087, 4585, 1187, 24365, 19335, 19337, 19338, 19339,
		19340, 19336, 25320, 19341, 19342, 19343, 19344, 19345, 25321,

}, itemNames = {})
public class OrnamentKitsItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		if (option == 3) {
			Kits kit = getMatchedKit(item);
			if (kit == null)
				return;
			if (!player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
				return;
			}
			player.getInventory().deleteItem(item);
			player.getInventory().addItem(kit.fromId, 1);
			player.getInventory().addItem(kit.kitId, 1);
			player.getPackets().sendGameMessage("You split the " + item.getDefinitions().getName() + ".");
		}
	}

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		Kits kit = getKit(firstItem, secondItem);
		if (kit == null)
			return;
		player.getInventory().deleteItem(slot, firstItem);
		player.getInventory().replaceItems(secondItem, new Item(kit.toId));
		player.getPackets().sendGameMessage(
				"You attach the kit to the " + ItemDefinitions.getItemDefinitions(kit.fromId).getName() + ".");
	}

	private static Kits getKit(Item item1, Item item2) {
		for (Kits kit : Kits.values()) {
			if (Inventory.contains(kit.kitId, kit.fromId, item1, item2))
				return kit;
		}
		return null;
	}

	private static Kits getMatchedKit(Item item) {
		for (Kits kit : Kits.values()) {
			if (kit.toId == item.getId())
				return kit;
		}
		return null;
	}

	@AllArgsConstructor
	private enum Kits {
		FURY_ORNAMENT(6585, 19333, 19335), DRAGON_PLATEBODY_OR(14479, 19350, 19337),
		DRAGON_PLATELEGS_OR(4087, 19348, 19338), DRAGON_PLATESKIR_OR(4585, 19348, 19339),
		DRAGON_SQ_SHIELD_OR(1187, 19352, 19340), DRAGON_FULL_HELM_OR(11335, 19346, 19336),
		DRAGON_KITESHIELD_OR(24365, 25312, 25320), DRAGON_FULL_HELM_SP(11335, 19354, 19341),
		DRAGON_PLATEBODY_SP(14479, 19358, 19342), DRAGON_PLATELEGS_SP(4087, 19356, 19343),
		DRAGON_PLATESKIR_SP(4585, 19356, 19344), DRAGON_SQ_SHIELD_SP(1187, 19360, 19345),
		DRAGON_KITESHIELD_SP(24365, 25314, 25321),;

		private int kitId, fromId, toId;
	}
}