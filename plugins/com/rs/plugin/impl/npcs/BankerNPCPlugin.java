package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = { "Banker"}, npcId = {2271})
public class BankerNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		switch (option) {
		case 1:
			player.getDialogueInterpreter().open(4907);
			break;
		case 2:
			player.getBank().openBank();
			break;

		}
	}
}