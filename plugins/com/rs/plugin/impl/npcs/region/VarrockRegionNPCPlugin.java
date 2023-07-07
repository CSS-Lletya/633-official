package com.rs.plugin.impl.npcs.region;

import java.util.ArrayList;
import java.util.List;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.content.quests.ScrollInterface;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Expression;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

import skills.herblore.BobBarter;
import skills.woodcutting.sawmill.Sawmill;

@NPCSignature(name = {}, npcId = { 4250, 6524 })
public class VarrockRegionNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (npc.getId() == 4250) {
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
		if (npc.getId() == 6524) {
			if (option == 1) {
				player.dialog(new DialogueEventListener(player, npc) {
					@Override
					public void start() {
						npc(happy, "Good day, how can I help you?");
						option("Decant my potions", () -> {
							npc(happy, "There, all done.");
							new BobBarter(player).decant();
						}, "Get latest grimy herb prices", () -> execute(player, npc, 2),
								"Nevermind", this::complete);
						
					}
				});
			}
			if (option == 2) {
				player.dialogue(npc.getId(), d -> d.npc(Expression.happy, "One moment please..."));
				List<String> messages = new ArrayList<String>();
				for (int i = 199; i <= 14836; i++) {
					ItemDefinitions herbs = ItemDefinitions.getItemDefinitions(i);
					if (!herbs.getName().contains("Grimy ") || herbs.isNoted())
						continue;
					messages.add(herbs.getName() + " - " + herbs.getFormatedItemValue() +"gp <br>");
				}
				String[] info = messages.toArray(new String[messages.size()]);
				ScrollInterface.sendQuestScroll(player, "Current Herb price guide", info);
			}
			if (option == 3) {
				player.dialogue(npc.getId(), d -> d.npc(Expression.happy, "One moment please..."));
				new BobBarter(player).decant();
			}
		}
	}
}