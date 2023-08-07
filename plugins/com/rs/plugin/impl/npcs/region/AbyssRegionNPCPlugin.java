package com.rs.plugin.impl.npcs.region;

import java.util.stream.IntStream;

import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

/**
 * Dialogue
 * @author Dennis
 *
 */
@NPCSignature(name = {}, npcId = { 2262 })
public class AbyssRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			//TODO: real dialogue
			if (!player.ownsItems(new Item(5509))) {
				player.dialogue(npc.getId(), d -> {
					d.npc(Mood.eyes_side2side_reading, "You're looking for a small pouch to hold your essences?");
					d.player(Mood.happy_plain_eyebrows_up, "Yes..");
					d.npc(Mood.annoyed, "Fine, take one.");
					d.event(() -> {
						if (player.getInventory().addItem(new Item(5509))) {
							d.npc(Mood.annoyed, "There you go, now leave me alone!");
							d.complete();
						} else 
							d.complete();
					});

				});
			} else {
				player.dialogue(npc.getId(), d -> d.npc(Mood.angry_2, "Leave me alone!"));
			}
		}
		if (option == 2) {
			if (!player.getInventory().containsAny(5511, 5513, 5515)) {
				player.dialogue(npc.getId(), d -> d.npc(Mood.sad, "You don't seem to have any pouches in need of repair. Leave me alone!"));
				return;
			}
			IntStream.of(5511, 5513, 5515).filter(pouch -> player.getInventory().containsAny(pouch))
			.forEach(pouch -> player.getInventory().replaceItems(new Item(pouch), new Item(pouch - 1)));
			player.dialogue(npc.getId(), d -> d.npc(Mood.happy, "There, I have repaired your pouches. Now leave me alone. I'm concentrating!"));
		}
	}
}