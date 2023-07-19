package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.IntegerInputAction;

import skills.crafting.Tanning;

@RSInterfaceSignature(interfaceId = { 324 })
public class TanningInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (packetId == 9) {
			player.getPackets().sendInputIntegerScript("How many hides would you like to tan?", new IntegerInputAction() {
				@Override
				public void handle(int input) {
					Tanning.handleTanning(player, componentId-1, packetId, input);
				}
			});
		} else
			Tanning.handleTanning(player, componentId-1, packetId, 0);
	}
}