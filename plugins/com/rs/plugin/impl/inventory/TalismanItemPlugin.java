package com.rs.plugin.impl.inventory;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.TeleportType;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.runecrafting.Altar;

/**
 * Just a simple way to teleport to the altars (custom)
 * A neat interface to check out: 780 (needs configs I suppose?)
 * @author Dennis
 *
 */
@InventoryWrapper(itemId = {1438, 1448, 1444, 1440, 1442, 1446, 1454, 1452, 1462, 1458, 1456, 1450}, itemNames = {})
public class TalismanItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if(option == 6) {
			Arrays.stream(Altar.values()).filter(altar -> altar.getTalisman() == item.getId())
			.forEach(altar -> player.getMovement().move(true, altar.getWorldTile(), TeleportType.NORMAL));
		}
	}
}