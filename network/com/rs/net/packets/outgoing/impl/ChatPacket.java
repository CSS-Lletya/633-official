package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.Huffman;
import com.rs.net.encoders.other.PublicChatMessage;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

@OutgoingPacketSignature(packetId = 54, description = "Represents a public message being sent by the Player")
public class ChatPacket implements OutgoingPacket {

	private int chatType;
	
	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted())
			return;
		if (player.getLastPublicMessage() > Utility.currentTimeMillis())
			return;
		player.setLastPublicMessage(Utility.currentTimeMillis() + 300);
		int colorEffect = stream.readUnsignedByte();
		int moveEffect = stream.readUnsignedByte();
		String message = Huffman.readEncryptedMessage(200, stream);
		if (message == null || message.replaceAll(" ", "").equals(""))
			return;
		if (message.startsWith("::") || message.startsWith(";;")) {
			// if command exists and processed wont send message as public
			// message
			CommandPacket.processCommand(player, message.replace("::", "")
					.replace(";;", ""), false, false);
			return;
		}
		if (player.getDetails().getMuted() > Utility.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You temporary muted. Recheck in 48 hours.");
			return;
		}
		int effects = (colorEffect << 8) | (moveEffect & 0xff);
//		if (chatType == 1)
//			player.sendFriendsChannelMessage(new ChatMessage(message));
//		else if (chatType == 2)
//			player.sendClanChannelMessage(new ChatMessage(message));
//		else if (chatType == 3)
//			player.sendGuestClanChannelMessage(new ChatMessage(message));
//		else
		PublicChatMessage chatMessage = new PublicChatMessage(message, effects);
		chatMessage.sendPublicChatMessage(player, chatMessage);
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Chat type: " + chatType);
	}
}