package com.rs.net.packets.outgoing.impl;

import java.util.List;

import com.rs.GameConstants;
import com.rs.constants.Sounds;
import com.rs.game.dialogue.DialogueAction;
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
		
		if (interfaceId == 740)
			player.getInterfaceManager().closeChatBoxInterface();
		
		if (interfaceId == 94) {
			if (buttonId == 3) 
				player.getInventory().deleteItem(new Item(player.getAttributes().get(Attribute.DESTROY_ITEM_ID).getInt()));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getAttributes().get(Attribute.DESTROY_ITEM_ID).set(null);
			player.getAudioManager().sendSound(Sounds.DESTOY_ITEM);
			return;
		}
		
		if (player.getDialogueInterpreter().getDialogue() == null && player.getDialogueInterpreter().getDialogueStage() == null) {
			player.getInterfaceManager().closeChatBoxInterface();
			if (DialogueEventListener.continueBlankDialogue(player, componentId))
				return;
			if(DialogueEventListener.continueDialogue(player, componentId))
				return;
			if (EniolaBanker.sendInterfaceFunctionality(player, componentId))
				return;
			List<DialogueAction> actions = player.getDialogueInterpreter().getActions();
			if (actions.size() > 0) {
				DialogueAction action = actions.get(0);
				action.handle(player, buttonId);
				actions.remove(action);
				actions.clear();
			}
		}
		player.getDialogueInterpreter().handle(componentId, buttonId);
	}
}