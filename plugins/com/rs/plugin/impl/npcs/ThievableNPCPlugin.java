package com.rs.plugin.impl.npcs;

import java.util.stream.IntStream;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCType;
import com.rs.plugin.wrapper.NPCSignature;

import skills.thieving.impl.Pickpocketing;
import skills.thieving.impl.Pickpocketing.PickpocketData;

@NPCSignature(name = {}, npcId = { 1, 2, 3, 4, 5, 6, 7, 1757, 1758, 1759, 1760, 1761, 1715, 1714, 1710, 1711, 1712, 15,
		18, 187, 9, 10, 5920, 3408, 1880, 1881, 1926, 1927, 1928, 1929, 1930, 1931, 23, 26, 1883, 1884, 32, 1904, 1905,
		20, 365, 2256, 66, 67, 68, 2234, 2235, 3299, 21, 7882 })
public class ThievableNPCPlugin implements NPCType {
	
	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		for (PickpocketData data : PickpocketData.values()) {
			Pickpocketing thieving = new Pickpocketing(player, data, npc);
			IntStream.of(data.npcId).filter(mob -> mob == npc.getId()).forEach(mob -> thieving.start());
		}
	}
}