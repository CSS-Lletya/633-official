package com.rs.plugin.impl.interfaces;

import java.util.Arrays;

import com.rs.game.player.Player;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.StringInputAction;

@RSInterfaceSignature(interfaceId = { 34 })
public class NotesInterfacePlugin extends RSInterfaceListener {

	private final String[][] EASTER_EGGS = {
			{"All your base are belong to us", "orly?"},
			{"orly?", "yarly"},
			{"Murder", "All rest and no play makes Guthix a dull boy."},
			{"Redrum", "All rest and no play makes Guthix a dull boy."},
			{"Andrew", "Cabbage."},
			{"Paul", "Rargh, I'm a lava monster!"},
			{"I am your father", "Nooooooooooooooooooooooooo!"},
			{"I'll be back", "Come with me if you want to live."},
			{"Finish the fight", "They must love the smell of hero."},
			{"There is no spoon", "Then you will see, it is not the spoon that bends, it is only yourself."},
			{"You fight like a dairy farmer", "How appropriate. You fight like a cow."},
			{"Bangin'", "donk"},
			{"Humperdinck", "Have fun storming the castle!"},
			{"Humperdink", "Have fun storming the castle!"},
			{"Milton Waddams", "The ratio of people to cake is too big."},
			{"R.I.P. Runescape", "Wanna bet?"},
			{"Penso, logo existo", "Borboletas salpicadas de goiabada..."},
			{"Le temps passe", "L'œuf dur."},
			{"Sevga", "Marmaros had a close encounter with a prayer-eating behemoth."},
			{"Maz", "Squirrels are watching you."},
	};
	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		switch (componentId) {
		case 35:
		case 37:
		case 39:
		case 41:
			player.getNotes().colour((componentId - 35) / 2);
			player.getPackets().sendHideIComponent(34, 16, true);
			break;
		case 3:
			player.getPackets().sendInputStringScript("Add note:", new StringInputAction() {
				@Override
				public void handle(String input) {
					if (Arrays.stream(EASTER_EGGS).anyMatch(message -> input.toLowerCase() == message[0].toLowerCase())) {
						Arrays.stream(EASTER_EGGS).filter(message -> input.toLowerCase() == message[0].toLowerCase())
								.forEach(message -> player.getNotes().add(message[1]));
					} else
						player.getNotes().add(input);
				}
			});
			break;
		case 9:
			switch (packetId) {
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
				if (player.getNotes().getCurrentNote() == slotId)
					player.getNotes().removeCurrentNote();
				else
					player.getNotes().setCurrentNote(slotId);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
				player.getNotes().setCurrentNote(slotId);
				player.getPackets().sendInputStringScript("Edit note:", new StringInputAction() {
					@Override
					public void handle(String input) {
						player.getNotes().edit(input);
					}
				});
				break;
			case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
				player.getNotes().setCurrentNote(slotId);
				player.getPackets().sendHideIComponent(34, 16, false);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
				player.getNotes().delete(slotId);
				break;
			}
			break;
		case 8:
			switch (packetId) {
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
				player.getNotes().delete();
				break;
			case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
				player.getNotes().deleteAll();
				break;
			}
			break;
		}
	}

}