package com.rs.plugin.impl.interfaces;

import com.rs.game.item.Item;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.ItemExamines;

@RSInterfaceSignature(interfaceId = { 667 })
public class EquipmentBonusesInterfacePlugin implements RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (packetId == 11) {
			if (slotId == 3)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_WEAPON);
			if (slotId == 0)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_HAT);
			if (slotId == 1)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_CAPE);
			if (slotId == 2)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_AMULET);
			if (slotId == 13)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_ARROWS);
			if (slotId == 4)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_CHEST);
			if (slotId == 7)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_LEGS);
			if (slotId == 9)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_HANDS);
			if (slotId == 10)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_FEET);
			if (slotId == 12)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_RING);
			if (slotId == 5)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_SHIELD);
			else if (componentId == 48)
				player.getBank().openBank();
		}
		if (componentId == 7 && packetId == 12) {
			Item item = player.getEquipment().getItem(slotId);
			if (item == null)
				return;
			player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
		}
		if (packetId == 74 && componentId == 7) {
			if (slotId >= player.getInventory().getItemsContainerSize())
				return;
			Item item = player.getEquipment().getItem(slotId);
			if (item == null)
				return;
			EquipmentInterfacePlugin.sendItemStats(player, item);	
		}
	}
}