package com.rs.plugin.impl.npcs.region;

import com.rs.constants.NPCNames;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCType;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {}, npcId = {NPCNames.ENIOLA_6362})
public class ZMIRegionNPCPlugin implements NPCType{

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		player.getInterfaceManager().sendInterface(619);
	}
}