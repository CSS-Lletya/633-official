package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Rest;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = { "Musician" }, npcId = {8722})
public class MusicianNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1)
			player.getDialogueInterpreter().open(29);
		else
			player.getAction().setAction(new Rest(4));
	}
}