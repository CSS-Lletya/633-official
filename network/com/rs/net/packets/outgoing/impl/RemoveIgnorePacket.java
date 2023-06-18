package com.rs.net.packets.outgoing.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

//TODO: Convert Packet
@OutgoingPacketSignature(packetId = -1, description = "Represents removing another Player from the Player's Ignore List")
public class RemoveIgnorePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		player.getFriendsIgnores().removeIgnore(stream.readString());
	}
}