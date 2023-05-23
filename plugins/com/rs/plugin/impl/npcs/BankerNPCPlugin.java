package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCType;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {"Banker"}, npcId = {})
public class BankerNPCPlugin implements NPCType{

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		if (npc.getDefinitions().hasOptions("Bank", "Talk-to") && option == 1 || option == 2) {
			player.getBank().openBank();
		}
	}
}