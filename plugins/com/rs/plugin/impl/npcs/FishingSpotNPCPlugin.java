package com.rs.plugin.impl.npcs;

import java.util.Optional;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.fishing.Fishing;
import skills.fishing.Tool;

@NPCSignature(name = { "Fishing spot", "Cavefish shoal"}, npcId = {})
public class FishingSpotNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC mob, int option){
		if (option == 1) {
			for (int i = 309; i < 318; i++) {
				if (i == 312 || i == 313 || i == 316)
					continue;
				if (mob.getId() == i) {
					new Fishing(player, Tool.LOBSTER_POT, Optional.of(mob.getLastWorldTile())).start();
				}
			}
			for (int i = 233; i <= 236; i++) {
				if (mob.getId() == i) {
					new Fishing(player, Tool.FLY_FISHING_ROD, Optional.of(mob.getLastWorldTile())).start();
				}
			}
			switch (mob.getId()) {
			case 6267:
				new Fishing(player, Tool.CRAYFISH_CAGE, Optional.of(mob.getLastWorldTile())).start();
				break;
			case 329:
				new Fishing(player, Tool.FLY_FISHING_ROD, Optional.of(mob.getLastWorldTile())).start();
				break;
			case 312:
				new Fishing(player, Tool.BIG_NET, Optional.of(mob.getLastWorldTile())).start();
				break;
			case 313:
				new Fishing(player, Tool.NET, Optional.of(mob.getLastWorldTile())).start();
				break;
			case 316:
			case 319:
				new Fishing(player, Tool.NET_MONKFISH, Optional.of(mob.getLastWorldTile())).start();
				break;
			case 322:
				new Fishing(player, Tool.FISHING_ROD, Optional.of(mob.getLastWorldTile())).start();
				break;
			case 8841:
			case 8842:
				new Fishing(player, Tool.FISHING_ROD_, Optional.of(mob.getLastWorldTile())).start();
				break;
			}
		}
		if (option == 2) {
			for (int i = 309; i < 319; i++) {
				if (i == 312 || i == 313 || i == 316)
					continue;
				if (mob.getId() == i) {
					new Fishing(player, Tool.HARPOON, Optional.of(mob.getLastWorldTile())).start();
				}
			}
			if (mob.getId() == 312 || mob.getId() == 322) {
				 new Fishing(player, Tool.SHARK_HARPOON, Optional.of(mob.getLastWorldTile())).start();
			}
			if (mob.getId() == 322 || mob.getId() == 329) {
				new Fishing(player, Tool.FISHING_ROD, Optional.of(mob.getLastWorldTile())).start();
			}
		}
	}
}