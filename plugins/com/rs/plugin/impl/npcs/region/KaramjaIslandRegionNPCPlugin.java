package com.rs.plugin.impl.npcs.region;

import java.util.stream.IntStream;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {"Stiles"}, npcId = {})
public class KaramjaIslandRegionNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			player.dialogue(new DialogueEventListener(player, npc) {
				@Override
				public void start() {
					npc(happy, "Ay-uh, 'tis a grand day for the fishin'.<br>Will ye wantin' to exchange yer fish for banknotes?");
				}
			});
		}
		if (option == 2) {
			if (!player.getInventory().containsAny(377, 371, 359)) {
				player.getPackets().sendGameMessage("You don't have any raw fish to exchange.");
				return;
			}
			for (Item fish : player.getInventory().getItems().getItemsCopy()) {
				if (fish == null)
					continue;
				if (IntStream.of(377, 371, 359).anyMatch(id -> fish.getId() == id))
					player.getInventory().replaceItems(fish, new Item(fish.getId() + 1));
			}
		}
	}
}