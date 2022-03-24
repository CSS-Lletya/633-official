package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

//TODO: Convert Packet
@OutgoingPacketSignature(packetId = -1, description = "Represents a change in state for a Friend on the Player's Friends List")
public class ChangeFriendsChatPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted()
				|| !player.getInterfaceManager().containsInterface(1108))
			return;
		player.getFriendsIgnores().changeRank(stream.readString(),
				stream.readUnsignedByte128());
	}
}