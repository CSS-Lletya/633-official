package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.DiangoHolidayItemRetrieval;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.ItemExamines;

@RSInterfaceSignature(interfaceId = { 468 })
public class DiangoHolidayItemRetrievalInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		switch (packetId) {
		case 11:
			DiangoHolidayItemRetrieval.removeHolidayItem(player, slotId);
			break;
		case 29:
			player.getPackets().sendGameMessage(ItemExamines.getExamine(slotId2));
			break;
		}
	}
}