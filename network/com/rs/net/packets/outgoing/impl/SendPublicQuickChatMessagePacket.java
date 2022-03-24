package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.Utility;
import com.rs.utilities.LogUtility.LogType;

@OutgoingPacketSignature(packetId = 69, description = "Represents sending a Public Quick-Chat based message")
public class SendPublicQuickChatMessagePacket implements OutgoingPacket {

	private int chatType;
	
	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted())
			return;
		if (player.getLastPublicMessage() > Utility.currentTimeMillis())
			return;
		player.setLastPublicMessage(Utility.currentTimeMillis() + 300);
		// just tells you which client script created packet
		@SuppressWarnings("unused")
		boolean secondClientScript = stream.readByte() == 1;// script 5059
		// or 5061
		int fileId = stream.readUnsignedShort();
		if (!Utility.isQCValid(fileId))
			return;
		byte[] data = null;
		if (stream.getLength() > 3) {
			data = new byte[stream.getLength() - 3];
			stream.readBytes(data);
		}
		data = Utility.completeQuickMessage(player, fileId, data);
//		if (chatType == 0)
//			player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
//		else if (chatType == 1)
//			player.sendFriendsChannelQuickMessage(new QuickChatMessage(
//					fileId, data));
		 if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Unknown chat type: " + chatType);
	}
}