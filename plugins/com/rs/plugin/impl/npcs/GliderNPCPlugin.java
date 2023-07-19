package com.rs.plugin.impl.npcs;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.traveling.Gliders;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {}, npcId = { 1800, 5249, 3811, 3809, 3810 })
public class GliderNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			player.dialogue(new DialogueEventListener(player, npc) {
				@Override
				public void start() {
					npc(happy, "Fancy an adventure today?");
					player(happy, "hmmm");
					option("Sure!", () -> {
						player.getInterfaceManager().sendInterface(138);
						Gliders.sendConfig(npc, player);
					}, "Not today", this::complete);
				}
			});
		}
		if (option == 2) {
			player.getInterfaceManager().sendInterface(138);
			Gliders.sendConfig(npc, player);
		}
	}
}