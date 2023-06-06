package com.rs.plugin.impl.interfaces;

import com.rs.game.npc.other.Gravestone;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 652 })
public class GraveStonesInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (componentId == 34) {
			Gravestone.sendBuySelection(player, slotId);
		}
	}
}