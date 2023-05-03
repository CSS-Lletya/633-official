package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.Utility;
import com.rs.utilities.LogUtility.LogType;

@OutgoingPacketSignature(packetId = 61, description = "Represents an interaction with a Dialogue state")
public class DialogueContinuePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		int junk = stream.readShortLE128();
		int interfaceHash = stream.readIntV2();
		int interfaceId = interfaceHash >> 16;
		int buttonId = (interfaceHash & 0xFF);
		if (Utility.getInterfaceDefinitionsSize() <= interfaceId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		}
		if (!player.isRunning()
				|| !player.getInterfaceManager().containsInterface(
						interfaceId))
			return;
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Dialogue: " + interfaceId + ", " + buttonId
					+ ", " + junk);
		int componentId = interfaceHash - (interfaceId << 16);
		if (interfaceId == 740)
			player.getInterfaceManager().closeChatBoxInterface();
		if (DialogueEventListener.continueDialogue(player, componentId))
			return;
	}
}