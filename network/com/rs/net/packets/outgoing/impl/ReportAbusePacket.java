package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

//TODO: Convert Packet
@OutgoingPacketSignature(packetId = -1, description = "Represents a event where a Player is reporting another Player")
public class ReportAbusePacket implements OutgoingPacket {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted())
			return;
		String displayName = stream.readString();
		int type = stream.readUnsignedByte();
		boolean mute = stream.readUnsignedByte() == 1;
		String unknown2 = stream.readString();
	}
}