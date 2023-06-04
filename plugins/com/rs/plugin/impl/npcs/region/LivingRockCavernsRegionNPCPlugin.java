package com.rs.plugin.impl.npcs.region;

import com.rs.game.map.GameObject;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.mining.Mining;
import skills.mining.RockData;

@NPCSignature(name = {}, npcId = {8837, 8838, 8839})
public class LivingRockCavernsRegionNPCPlugin implements NPCListener{

	@Override
	public void execute(Player player, NPC npc, int option) {
		GameObject rocks = new GameObject(npc.getId(), 1, 10, npc);
		new Mining(player, RockData.LRC_MINERALS, rocks).start();
	}
}