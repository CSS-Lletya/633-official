package com.rs.net.packets.outgoing.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.Huffman;
import com.rs.net.encoders.other.ChatMessage;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.Utility;

@OutgoingPacketSignature(packetId = 40, description = "Represents sending a Message to another Player (Privately)")
public class SendFriendMessagePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		if (player.getDetails().getMuted() > Utility.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You temporary muted. Recheck in 48 hours.");
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
				new ChatMessage(Huffman.readEncryptedMessage(150, stream)));
	}
}