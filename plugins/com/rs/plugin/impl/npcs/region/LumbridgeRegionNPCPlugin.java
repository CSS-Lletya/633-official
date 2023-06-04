package com.rs.plugin.impl.npcs.region;

import java.util.stream.IntStream;

import com.rs.constants.NPCNames;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {}, npcId = {2238,2244, NPCNames.COOK_278, NPCNames.SERGEANT_ABRAM_7888})
public class LumbridgeRegionNPCPlugin implements NPCListener{

	@Override
	public void execute(Player player, NPC npc, int option) {
		IntStream.of(278, 2238,2244).anyMatch(id -> player.getDialogueInterpreter().open(npc.getId()));
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
	}
}