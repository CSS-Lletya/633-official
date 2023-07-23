package com.rs.plugin.impl.npcs.region;

import com.rs.content.mapzone.impl.SorceresssGardenMapZone;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Mood;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.Skills;

@NPCSignature(name = { "Ellis" }, npcId = { 5532, 924 })
public class AlkhraidRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (npc.getId() == 5532) {
			if (option == 3) {
				SorceresssGardenMapZone.teleportToSorceressGardenNPC(npc, player);
				return;
			}
			player.dialogue(new DialogueEventListener(player, npc) {
				@Override
				public void start() {
					player(Mood.happy, "Hey apprentice, do you want ", " to try out your teleport skills again?");
					npc(Mood.annoyed, "Okay, here goes - and remember ",
							" to return just drink from the fountain.");
					option("Teleport to the Sorceresss Garden",
							() -> SorceresssGardenMapZone.teleportToSorceressGardenNPC(npc, player),
							"Nevermind", () -> complete());
				}
			});
			return;
		}
		if (npc.getId() == 924) {
			if (player.getInventory().containsOneItem(10848, 10849, 10850, 10851)) {
				player.dialogue(npc.getId(), d -> {
					d.player(Mood.happy, "I have some sq'irk juice for you.");
					d.mes("Osman imparts some Thieving advice "," to you as a reward for the sq'irk juice.");
					d.event(() -> {
						int totalXp =
						player.getInventory().getNumberOf(10851) * 350;
		                totalXp += player.getInventory().getNumberOf(10848) * 1350;
		                totalXp += player.getInventory().getNumberOf(10850) * 2350;
		                totalXp += player.getInventory().getNumberOf(10849) * 3000;
		                player.getInventory().deleteItem(10848, player.getInventory().getNumberOf(10848));
		                player.getInventory().deleteItem(10849, player.getInventory().getNumberOf(10849));
		                player.getInventory().deleteItem(10850, player.getInventory().getNumberOf(10850));
		                player.getInventory().deleteItem(10851, player.getInventory().getNumberOf(10851));
		                player.getSkills().addExperience(Skills.THIEVING, totalXp);
					});
				});
	        } 
			return;
		}
		player.getInterfaceManager().sendInterface(324);//tanner
	}
}