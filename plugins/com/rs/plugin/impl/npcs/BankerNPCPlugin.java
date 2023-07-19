package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = { "Banker"}, npcId = {2271})
public class BankerNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		switch (option) {
		case 1:
			player.getDialogueInterpreter().open(6200);
			break;
		case 2:
			player.getBank().openBank();
			break;
		}
	}
}