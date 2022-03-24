package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacket;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.plugin.NPCPluginDispatcher;

@LogicPacketSignature(packetId = 80, packetSize = 3, description = "The Fourth menu option for a NPC")
public class NPCFourthClickPacket implements LogicPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		NPCPluginDispatcher.executeMobInteraction(player, stream, 4);
	}
}