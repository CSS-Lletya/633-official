package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.plugin.NPCPluginDispatcher;

@LogicPacketSignature(packetId = 24, packetSize = 3, description = "The Second menu option for a NPC")
public class NPCSecondClickPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		NPCPluginDispatcher.executeMobInteraction(player, stream, 2);
	}
}