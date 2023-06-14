package com.rs.plugin.impl.npcs.region;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

import skills.woodcutting.Sawmill;

@NPCSignature(name = {}, npcId = {4250})
public class VarrockRegionNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			player.dialog(new DialogueEventListener(player, npc) {
				@Override
				public void start() {
					npc(happy, "If you bring'em, i'll mill them! ..get it? no okay.");
				}
			});
		}
		if (option == 2) {
			player.getInterfaceManager().closeInterfaces();
			Sawmill.openPlanksConverter(player);
		}
		if (option == 3) {
			String key = ShopsHandler.getShopForNpc(npc.getId());
			ShopsHandler.openShop(player, key);
		}
	}
}