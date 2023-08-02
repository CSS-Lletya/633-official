package com.rs.plugin.impl.interfaces;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 670 })
public class EquipmentBonusesInventoryInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (componentId == 0) {
			if (slotId >= player.getInventory().getItemsContainerSize())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				return;
			if (packetId == 11) {
				if (RSInterfacePluginDispatcher.sendWear(player, slotId, item.getId()))
					RSInterfacePluginDispatcher.refreshEquipBonuses(player);
			} else if (packetId == 31)
				player.getInventory().sendExamine(slotId);
			else if (packetId == 29) {
				EquipmentInterfacePlugin.sendItemStats(player, item);
			}
		}
	}
}