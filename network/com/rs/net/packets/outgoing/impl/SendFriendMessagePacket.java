package com.rs.net.packets.outgoing.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.Huffman;
import com.rs.net.encoders.other.ChatMessage;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 40, description = "Represents sending a Message to another Player (Privately)")
public class SendFriendMessagePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		if (HostManager.contains(player.getUsername(), HostListType.MUTED_IP)) {
			return;
		}
		String username = stream.readString();
		Player p2 = World.getPlayerByDisplayName(username);
		if (p2 == null) {
			p2 = World.getLobbyPlayerByDisplayName(username);
			if (p2 == null) {
				return;
			}
		}

		player.getFriendsIgnores().sendMessage(p2,
				new ChatMessage(player, Huffman.readEncryptedMessage(150, stream)));
	}
}