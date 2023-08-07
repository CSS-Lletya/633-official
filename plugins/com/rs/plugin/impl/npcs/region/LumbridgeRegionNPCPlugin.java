package com.rs.plugin.impl.npcs.region;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.constants.NPCNames;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Mood;
import com.rs.game.npc.NPC;
import com.rs.game.npc.other.Gravestone;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import lombok.val;

@NPCSignature(name = {}, npcId = {NPCNames.HANS_7935, 4903, 2238,2244, NPCNames.COOK_278, NPCNames.SERGEANT_ABRAM_7888})
public class LumbridgeRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		IntStream.of(278, 2238,2244).anyMatch(id -> player.getDialogueInterpreter().open(npc.getId()));
		if (npc.getId() == 4903)
			Gravestone.sendInterface(player);
		if (npc.getId() == NPCNames.SERGEANT_ABRAM_7888) {
			player.dialogue(dialogue -> {
				dialogue.option
				("Join the developments Discord Server", () -> {
					player.getPackets().sendOpenURL("https://discord.gg/zuqCZhHQaG");
				}, "View open-633 source code", () -> {
					player.getPackets().sendOpenURL("https://github.com/CSS-Lletya/633-official");
				});
			});
		}
		if (npc.getId() == NPCNames.HANS_7935) {//hans
			val seconds = (int) (player.getDetails().getPlayTime().get() * 0.6);
			val days = seconds / 86400;
			val hours = (seconds / 3600) - (days * 24);
			val minutes = (seconds / 60) - (days * 1440) - (hours * 60);
			
			player.dialogue(new DialogueEventListener(player, npc) {
				@Override
				public void start() {
					npc(Mood.happy, "Hello. What are you doing here?");
					option(
							"I'm looking for whoever is in charge of this place", () -> {
								player(Mood.happy, "I'm looking for whoever is in charge of this place.");
								npc(Mood.happy, "Who, the Duke? He's in his study, on the first floor.");
							}, 
							"I have come to kill everyone in this castle!", () -> player(Mood.happy, "I have to come to kill everyone in this castle!"), 
							"don't know. I'm lost. Where am I?", () -> {
								player(Mood.happy, "I don't know. I'm lost. Where am I?");
								npc(Mood.happy, "You are in Lumbridge Castle.");
							},
							"Can you tell me how long I've been here?", () -> {
								npc(Mood.happy, "You've spent " + days + (days > 1 || days == 0 ? " days" : " day") + ", " + hours + (hours > 1 || hours == 0 ? " hours" : " hour") + ", " + minutes + (minutes > 1 || minutes == 0 ? " minutes" : " minute") + " in the world since you arrived in " + GameConstants.SERVER_NAME + ".");
							},
							"Nothing", () -> player(Mood.happy, "Nothing."));
				}
			});
		}
	}
}