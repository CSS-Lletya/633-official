package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 17, description = "Represents removing another Player from the Player's Friends List")
public class RemoveFriendPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted())
			return;
		player.getFriendsIgnores().removeFriend(stream.readString());
	}
}