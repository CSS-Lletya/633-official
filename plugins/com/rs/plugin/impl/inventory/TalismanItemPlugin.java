package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.runecrafting.Runecrafting;

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
		int itemId = item.getId();
		if(option == 6) {
			if (itemId == 1438)
				Runecrafting.locate(player, 3127, 3405);
			else if (itemId == 1440)
				Runecrafting.locate(player, 3306, 3474);
			else if (itemId == 1442)
				Runecrafting.locate(player, 3313, 3255);
			else if (itemId == 1444)
				Runecrafting.locate(player, 3185, 3165);
			else if (itemId == 1446)
				Runecrafting.locate(player, 3053, 3445);
			else if (itemId == 1448)
				Runecrafting.locate(player, 2982, 3514);
			/*
			 * leaving this here if you want to make it so the item action "Locate" sends a teleport to the respective altar.
			 */
//			Arrays.stream(Altar.values()).filter(altar -> altar.getTalisman() == item.getId())
//			.forEach(altar -> player.getMovement().move(true, altar.getWorldTile(), TeleportType.NORMAL));
		}
	}
}