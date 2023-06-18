package com.rs.net.packets.outgoing.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.encoders.other.QuickChatMessage;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.Utility;

@OutgoingPacketSignature(packetId = 49, description = "Represents sending a Friend a Quick-Chat based message (Privately)")
public class SendFriendQuickChatMessagePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() && !World.containsLobbyPlayer(player.getUsername()))
			return;
		String username = stream.readString();
		int fileId = stream.readUnsignedShort();
		if (!Utility.isQCValid(fileId))
			return;
		byte[] data = null;
		if (stream.getLength() > 3 + username.length()) {
			data = new byte[stream.getLength() - (3 + username.length())];
			stream.readBytes(data);
		}
		data = Utility.completeQuickMessage(player, fileId, data);
		Player p2 = World.getPlayerByDisplayName(username);
		if (p2 == null) {
			p2 = World.getLobbyPlayerByDisplayName(username);
			if (p2 == null) {
				return;
			}
		}
		player.getFriendsIgnores().sendQuickChatMessage(p2,
				new QuickChatMessage(fileId, data));
	}
}