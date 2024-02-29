package com.rs.plugin.impl.npcs;

import java.util.stream.IntStream;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.fishing.Fishing;
import skills.fishing.Tool;

@NPCSignature(name = { "Fishing spot", "Cavefish shoal" }, npcId = {})
public class FishingSpotNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC mob, int option) {
		if (option == 1) {
			if (IntStream.of(309, 310, 311, 314, 315, 317, 319, 324).anyMatch(id -> mob.getId() == id))
				new Fishing(player, Tool.LOBSTER_POT, mob).start();
			if (IntStream.rangeClosed(233, 236).anyMatch(id -> mob.getId() == id)
					|| IntStream.of(329, 330, 323,318).anyMatch(id -> mob.getId() == id))
				new Fishing(player, Tool.FLY_FISHING_ROD, mob).start();
			switch (mob.getId()) {
			case 6267:
				new Fishing(player, Tool.CRAYFISH_CAGE, mob).start();
				break;
			case 312:
				new Fishing(player, Tool.BIG_NET, mob).start();
				break;
			case 313:
				new Fishing(player, Tool.NET, mob).start();
				break;
			case 316:
			case 319:
				new Fishing(player, Tool.NET_MONKFISH, mob).start();
				break;
			case 322:
				new Fishing(player, Tool.FISHING_ROD_, mob).start();
				break;
			case 8841:
			case 8842:// rocktails
				new Fishing(player, Tool.FISHING_ROD_, mob).start();
				break;
			case 1178:
				new Fishing(player, Tool.VESSEL, mob).start();
				break;
			case 1174:
				new Fishing(player, Tool.NET_KARAMBWANJI, mob).start();
				break;
			case 327:
				new Fishing(player, Tool.NET, mob).start();
				break;
			}
			
		}
		if (option == 2) {
			if (IntStream.of(309, 310, 311, 314, 315, 317, 319, 324).anyMatch(id -> mob.getId() == id))
				new Fishing(player, Tool.HARPOON, mob).start();
			if (mob.getId() == 312 || mob.getId() == 322) {
				new Fishing(player, Tool.SHARK_HARPOON, mob).start();
			}
			if (IntStream.of(329, 322, 330, 323,318).anyMatch(id -> mob.getId() == id))
				new Fishing(player, Tool.FISHING_ROD, mob).start();
			switch(mob.getId()) {
			case 327:
				new Fishing(player, Tool.FISHING_ROD, mob).start();
				break;
			}
		}
	}
}