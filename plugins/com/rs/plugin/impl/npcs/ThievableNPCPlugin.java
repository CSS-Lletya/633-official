package com.rs.plugin.impl.npcs;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.thieving.impl.Pickpocketing;
import skills.thieving.impl.Pickpocketing.PickpocketData;

@NPCSignature(name = {}, npcId = { 1, 2, 3, 4, 5, 6, 16, 24, 170, 351, 663, 728, 729, 1086, 3223, 3224, 3225, 3915,
		5923, 7873, 7874, 7875, 7876, 7877, 7878, 7879, 7909, 7910, 8010, 8011, 12345, 12346, 12347, 25, 352, 353, 354,
		360, 361, 362, 363, 2776, 3226, 5924, 7880, 7881, 7882, 7883, 7884, 8012, 8013, 7, 291, 1377, 1757, 1758, 1759,
		1760, 1761, 3917, 4925, 7128, 7129, 7130, 7131, 1715, 1714, 1716, 1710, 1711, 1712, 15, 18, 187, 2267, 2268,
		2269, 8122, 9, 10, 5920, 3408, 1880, 1881, 1926, 1927, 1928, 1929, 1930, 1931, 23, 26, 1883, 1884, 32, 1905, 20,
		365, 2256, 66, 67, 68, 21, 2234, 2235, 3299 })
public class ThievableNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		npc.doAction(option, "Pickpocket", () -> {
			Arrays.stream(PickpocketData.values()).forEach(data -> IntStream.of(data.npcId)
					.filter(mob -> mob == npc.getId())
					.forEach(mob -> new Pickpocketing(player, data, npc).start()));
		});
	}
}