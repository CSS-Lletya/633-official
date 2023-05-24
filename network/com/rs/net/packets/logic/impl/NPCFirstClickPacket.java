package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.plugin.NPCPluginDispatcher;

@LogicPacketSignature(packetId = 22, packetSize = 3, description = "The First menu option for a NPC")
public class NPCFirstClickPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		NPCPluginDispatcher.executeMobInteraction(player, stream, 1);
	}
}