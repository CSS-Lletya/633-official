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

@NPCSignature(name = {}, npcId = { 4250, 6521, 6522, 6523, 6524, 6525, 6526, 6527})
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
		if (npc.getId() == 6526) {
			if (option == 1) {
				player.dialog(new DialogueEventListener(player, npc) {
					@Override
					public void start() {
						npc(happy, "Good day, how can I help you?");
						option("View current Log price guide", () -> {
							List<String> messages = new ArrayList<String>();
							for (int i = 1511; i <= 12583; i++) {
								ItemDefinitions log = ItemDefinitions.getItemDefinitions(i);
								if (!log.getName().toLowerCase().contains("logs") || log.isNoted())
									continue;
								if (log.getName().equalsIgnoreCase("logs") && i != 1511)
									continue;
								messages.add(log.getName() + " - " + log.getFormatedItemValue() +"gp <br>");
							}
							String[] info = messages.toArray(new String[messages.size()]);
							ScrollInterface.sendQuestScroll(player, "Current Log price guide", info);
						}, "Nevermind", this::complete);
						
					}
				});
			}
			if (option == 2) {
				player.dialogue(npc.getId(), d -> d.npc(Expression.happy, "One moment please..."));
				List<String> messages = new ArrayList<String>();
				for (int i = 1511; i <= 12583; i++) {
					ItemDefinitions log = ItemDefinitions.getItemDefinitions(i);
					if (!log.getName().toLowerCase().contains("logs") || log.isNoted())
						continue;
					if (log.getName().equalsIgnoreCase("logs") && i != 1511)
						continue;
					messages.add(log.getName() + " - " + log.getFormatedItemValue() +"gp <br>");
				}
				String[] info = messages.toArray(new String[messages.size()]);
				ScrollInterface.sendQuestScroll(player, "Current Log price guide", info);
			}
		}
		if (npc.getId() == 6527) {
			player.dialog(new DialogueEventListener(player, npc) {
				@Override
				public void start() {
					npc(happy, "Good day, I'm quite busy right now.<br>Maybe we can chat later");
					//gonna skip this armor/weapons ordeal
				}
			});
		}
		if (npc.getId() == 6523) {
			if (option == 1) {
				player.dialog(new DialogueEventListener(player, npc) {
					@Override
					public void start() {
						npc(happy, "Good day, how can I help you?");
						option("View current Ores price guide", () -> {
							List<String> messages = new ArrayList<String>();
							for (int i = 436; i <= 668; i++) {
								ItemDefinitions ores = ItemDefinitions.getItemDefinitions(i);
								if (!ores.getName().toLowerCase().contains("ore") || ores.isNoted())
									continue;
								messages.add(ores.getName() + " - " + ores.getFormatedItemValue() +"gp <br>");
							}
							String[] info = messages.toArray(new String[messages.size()]);
							ScrollInterface.sendQuestScroll(player, "Current Ores price guide", info);
						}, "Nevermind", this::complete);
						
					}
				});
			}
			if (option == 2) {
				List<String> messages = new ArrayList<String>();
				for (int i = 436; i <= 668; i++) {
					ItemDefinitions ores = ItemDefinitions.getItemDefinitions(i);
					if (!ores.getName().toLowerCase().contains("ore") || ores.isNoted())
						continue;
					messages.add(ores.getName() + " - " + ores.getFormatedItemValue() +"gp <br>");
				}
				String[] info = messages.toArray(new String[messages.size()]);
				ScrollInterface.sendQuestScroll(player, "Current Ores price guide", info);
			}
		}
		
		if (npc.getId() == 6525) {
			if (option == 1) {
				player.dialog(new DialogueEventListener(player, npc) {
					@Override
					public void start() {
						npc(happy, "Good day, how can I help you?");
						option("View current Runes price guide", () -> {
							List<String> messages = new ArrayList<String>();
							for (int i = 554; i <= 9075; i++) {
								ItemDefinitions runes = ItemDefinitions.getItemDefinitions(i);
								if (!runes.getName().toLowerCase().contains("rune") || runes.isNoted())
									continue;
								if (i > 566 && i != 9075)
									continue;
								messages.add(runes.getName() + " - " + runes.getFormatedItemValue() +"gp <br>");
							}
							String[] info = messages.toArray(new String[messages.size()]);
							ScrollInterface.sendQuestScroll(player, "Current Runes price guide", info);
						}, "Nevermind", this::complete);
						
					}
				});
			}
			if (option == 2) {
				player.dialogue(npc.getId(), d -> d.npc(Expression.happy, "One moment please..."));
				List<String> messages = new ArrayList<String>();
				for (int i = 554; i <= 9075; i++) {
					ItemDefinitions runes = ItemDefinitions.getItemDefinitions(i);
					if (!runes.getName().toLowerCase().contains("rune") || runes.isNoted())
						continue;
					if (i > 566 && i != 9075)
						continue;
					messages.add(runes.getName() + " - " + runes.getFormatedItemValue() +"gp <br>");
				}
				String[] info = messages.toArray(new String[messages.size()]);
				ScrollInterface.sendQuestScroll(player, "Current Runes price guide", info);
			}
			if (option == 3) {
				player.dialogue(npc.getId(), d -> d.npc(Expression.sad, "I'm currently not able to do this right now, sorry."));
			}
		}
		if (npc.getId() == 6521) {
			player.dialogue(npc.getId(), d -> d.npc(Expression.annoyed, "How can I help you.. <br>It seems like you already know about this market."));
		}
		if (npc.getId() == 6522) {
			player.dialogue(npc.getId(), d -> d.npc(Expression.angry_2, "HEY I'M WORKING HERE!"));
		}
	}
}