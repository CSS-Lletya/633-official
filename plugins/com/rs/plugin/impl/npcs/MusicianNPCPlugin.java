package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Rest;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = { "Musician" }, npcId = {})
public class MusicianNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		npc.doAction(option, "Listen-to", () -> player.getAction().setAction(new Rest(player, 4)));
	}
}