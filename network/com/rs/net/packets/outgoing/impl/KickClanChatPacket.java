package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.Utility;

//TODO: Convert Packet
@OutgoingPacketSignature(packetId = -1, description = "Represents an event where a Player is being kicked from a Clan Chat")
public class KickClanChatPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted())
			return;
		player.setLastPublicMessage(Utility.currentTimeMillis() + 1000); // avoids
		// message
		// appearing
		boolean guest = stream.readByte() == 1;
		if (!guest)
			return;
		stream.readUnsignedShort();
//		player.kickPlayerFromClanChannel(stream.readString());
	}
}