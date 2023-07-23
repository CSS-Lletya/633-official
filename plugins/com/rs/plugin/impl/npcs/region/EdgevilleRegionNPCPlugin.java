package com.rs.plugin.impl.npcs.region;

import com.rs.game.npc.NPC;
import com.rs.game.npc.drops.Statuettes;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.Utility;

@NPCSignature(name = {}, npcId = {8725})
public class EdgevilleRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (npc.getId() == 8725) {
			player.dialogue(d -> d.option("Exchange all statues for " + Utility.getFormattedNumber(Statuettes.getTotalValue(player)) + " coins?" , () -> {
				Statuettes.exchangeStatuettes(player);
			}, "Nevermind", () -> d.complete()));
		}
	}
}