package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.constants.Sounds;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import skills.runecrafting.EniolaBanker;

@OutgoingPacketSignature(packetId = 61, description = "Represents an interaction with a Dialogue state")
public class DialogueContinuePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int junk = stream.readShortLE128();
		int interfaceHash = stream.readIntV2();
		int interfaceId = interfaceHash >> 16;
		int buttonId = (interfaceHash & 0xFF);
		if (Utility.getInterfaceDefinitionsSize() <= interfaceId) {
			 player.getSession().getChannel().close();
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
		switch (interfaceId) {
		case 94:
			if (buttonId == 3)
				player.getInventory().deleteItem(new Item(player.getAttributes().get(Attribute.DESTROY_ITEM_ID).getInt()));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getAttributes().get(Attribute.DESTROY_ITEM_ID).set(null);
			player.getAudioManager().sendSound(Sounds.DESTOY_ITEM);
			break;
		case 905:
			player.getInterfaceManager().closeChatBoxInterface();
			DialogueEventListener.continueBlankDialogue(player, componentId);
			break;
		case 619:
			EniolaBanker.sendInterfaceFunctionality(player, componentId);
			break;
		default:
			if (DialogueEventListener.continueDialogue(player, componentId))
				return;
			player.getDialogueInterpreter().handle(componentId, buttonId);
			break;
		}
	}
}