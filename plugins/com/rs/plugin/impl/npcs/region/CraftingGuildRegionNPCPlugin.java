package com.rs.plugin.impl.npcs.region;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {}, npcId = { 804 })
public class CraftingGuildRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		player.getInterfaceManager().sendInterface(324);//tanner
	}
}