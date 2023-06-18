package com.rs.net.packets.outgoing.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 7, description = "Represents adding another Player to the Player's Ignore List")
public class AddIgnorePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		player.getFriendsIgnores().addIgnore(stream.readString(),
				stream.readUnsignedByte() == 1);
	}
}