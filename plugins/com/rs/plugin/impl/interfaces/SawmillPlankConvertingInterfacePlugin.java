package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

import skills.woodcutting.sawmill.Sawmill;

@RSInterfaceSignature(interfaceId = { 403 })
public class SawmillPlankConvertingInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		Sawmill.handlePlanksConvertButtons(player, componentId, packetId);
	}
}