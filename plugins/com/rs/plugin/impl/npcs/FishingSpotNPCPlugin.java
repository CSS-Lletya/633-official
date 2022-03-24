package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCType;
import com.rs.plugin.wrapper.NPCSignature;

import skills.fishing.Fishing;
import skills.fishing.Tool;

@NPCSignature(name = { "Fishing spot" }, npcId = {})
public class FishingSpotNPCPlugin implements NPCType {

	@Override
	public void execute(Player player, NPC mob, int option) throws Exception {
		if (option == 1) {
			for (int i = 309; i < 318; i++) {
				if (i == 312 || i == 313 || i == 316)
					continue;
				if (mob.getId() == i) {
					Fishing lobster_pot = new Fishing(player, Tool.LOBSTER_POT, mob.getLastWorldTile());
					lobster_pot.start();
				}
			}
			for (int i = 233; i < 235; i++) {
				if (mob.getId() == i) {
					Fishing fly_fishing = new Fishing(player, Tool.FLY_FISHING_ROD, mob.getLastWorldTile());
					fly_fishing.start();
				}
			}
			switch (mob.getId()) {
			case 312:
				Fishing big_net = new Fishing(player, Tool.BIG_NET, mob.getLastWorldTile());
				big_net.start();
				break;
			case 313:
				Fishing net = new Fishing(player, Tool.NET, mob.getLastWorldTile());
				net.start();
				break;
			case 316:
			case 319:
				Fishing net_monkfish = new Fishing(player, Tool.NET_MONKFISH, mob.getLastWorldTile());
				net_monkfish.start();
				break;
			case 322:
				Fishing lobster_pot = new Fishing(player, Tool.FISHING_ROD, mob.getLastWorldTile());
				lobster_pot.start();
				break;
			}
		}
		if (option == 2) {
			for (int i = 309; i < 319; i++) {
				if (i == 312 || i == 313 || i == 316)
					continue;
				if (mob.getId() == i) {
					Fishing lobster_pot = new Fishing(player, Tool.HARPOON, mob.getLastWorldTile());
					lobster_pot.start();
				}
			}
			if (mob.getId() == 312 || mob.getId() == 322) {
				Fishing lobster_pot = new Fishing(player, Tool.SHARK_HARPOON, mob.getLastWorldTile());
				lobster_pot.start();
			}
		}
	}
}