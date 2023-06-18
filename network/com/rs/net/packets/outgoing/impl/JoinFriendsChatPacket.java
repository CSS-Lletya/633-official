package com.rs.net.packets.outgoing.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 1, description = "Represents a Player joining a Friend's Chat")
public class JoinFriendsChatPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		FriendChatsManager.joinChat(stream.readString(), player);
	}
}