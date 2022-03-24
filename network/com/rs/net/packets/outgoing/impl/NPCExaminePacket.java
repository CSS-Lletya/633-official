package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 15, description = "Represents an NPC's examine message and or event")
public class NPCExaminePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
//		NPCDispatcher.handleExamine(player, stream);
	}
}