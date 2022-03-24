package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 5, description = "Represents a Player's Camera movement state")
public class MoveCameraPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		stream.readUnsignedShort();
		stream.readUnsignedShort();
	}
}