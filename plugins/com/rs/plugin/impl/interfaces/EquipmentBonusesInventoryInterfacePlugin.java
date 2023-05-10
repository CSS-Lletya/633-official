package com.rs.plugin.impl.interfaces;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterface;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 670 })
public class EquipmentBonusesInventoryInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		System.out.println(slotId);
		if (componentId == 0) {
			if (slotId >= player.getInventory().getItemsContainerSize())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				return;
			if (packetId == 11) {
				if (RSInterfacePluginDispatcher.sendWear(player, slotId, item.getId()))
					RSInterfacePluginDispatcher.refreshEquipBonuses(player);
			} else if (packetId == 9)
				player.getInventory().sendExamine(slotId);
			else if (packetId == 31) {//broken 
				EquipmentBonusesInterfacePlugin.sendItemStats(player, item);
			}
		}
	}
}