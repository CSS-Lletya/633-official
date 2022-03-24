package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 67, description = "Represents an Object's examine message and or event")
public class ObjectExaminePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
//		ObjectDispatcher.handleOption(player, stream, -1);
	}
}