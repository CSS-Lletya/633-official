package com.rs.plugin.impl.npcs.region;

import com.rs.constants.NPCNames;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {}, npcId = {NPCNames.ENIOLA_6362})
public class ZMIRegionNPCPlugin implements NPCListener{

	@Override
	public void execute(Player player, NPC npc, int option) {
		player.getInterfaceManager().sendInterface(619);
	}
}