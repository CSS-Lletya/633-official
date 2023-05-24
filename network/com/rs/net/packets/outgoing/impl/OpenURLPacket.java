package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

//TODO: Convert Packet
@OutgoingPacketSignature(packetId = -1, description = "Represents a URL opening event")
public class OpenURLPacket implements OutgoingPacketListener {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, InputStream stream) {
		String type = stream.readString();
		String path = stream.readString();
		String unknown = stream.readString();
		int flag = stream.readUnsignedByte();
	}
}