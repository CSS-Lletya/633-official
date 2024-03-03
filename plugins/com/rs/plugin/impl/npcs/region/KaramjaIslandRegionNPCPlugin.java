package com.rs.plugin.impl.npcs.region;

import java.util.stream.IntStream;

import com.rs.constants.NPCNames;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = { "Stiles", "Lubufu" }, npcId = {})
public class KaramjaIslandRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (npc.getDefinitions().getName().equalsIgnoreCase("stiles")) {
			if (option == 1) {
				player.dialogue(new DialogueEventListener(player, NPCNames.STILES_11267) {
					@Override
					public void start() {
						npc(happy, "Ay-uh, 'tis a grand day for the fishin'. Will ye wantin' to exchange yer fish for banknotes?");
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

		if (npc.getDefinitions().getName().equalsIgnoreCase("Lubufu")) {
			player.dialogue(npc.getId(), d -> {
				d.npc(Mood.annoyed, "Watch where you're going, young whippersnapper!");
				d.option("I wasn't going anywhere...", () -> {
					d.player(Mood.happy_plain, "I wasn't going anywhere...");
					d.npc(Mood.annoyed, "Well then go away from here!");
				}, "What's a whippersnapper?", () -> {
					d.player(Mood.thinking_plain, "What's a whippersnapper?");
					d.npc(Mood.annoyed, "It's a whip. Which snaps. Like me. Now leave!");
				}, "Who are you?", () -> {
					d.npc(Mood.happy, "I am Lubufu - the only fisherman who knows the secret of the Karambwan!");
					d.player(Mood.thinking_plain, "What's a Karambwan?");
					d.npc(Mood.annoyed, "What a foolish question! Now leave!");
				}, "Can I have vessel to fish with?", () -> {
					if (player.ownsItems(new Item(3157), new Item(3159))) {
						d.npc(Mood.angry_2, "You already have one! Now leave!");
					} else {
						d.npc(Mood.annoyed, "Fine... here you are, don't lose it!");
						d.event(() -> {
							player.getInterfaceManager().closeChatBoxInterface();
							if (player.getInventory().addItem(new Item(3157))) {
								return;
							}
						});
					}
				});
			});
		}
	}
}