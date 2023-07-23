package com.rs.plugin.impl.npcs.region;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Mood;
import com.rs.game.npc.NPC;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

@NPCSignature(name = {}, npcId = {3820})
public class DraynorVillageRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		player.dialogue(new DialogueEventListener(player, npc) {
			@Override
			public void start() {
				npc(Mood.question, "Greetings! What can I do for you?");
				if (player.getDetails().getQuestPoints().get() == 0) {
					player(Mood.happy, "I was wondering if you could sell me a quest cape!"," I have completed all the quests.");
					npc(Mood.holy_shit, "Impressive! I see you have! It will ","cost you 99,000 coins, though.");
					option("Yes, I have that with me now.", () -> {
						if (player.getInventory().getFreeSlots() < 2) {
							player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
							this.complete();
							return;
						}
						if (player.getInventory().canPay(99000)) {
							event(() -> {
								player.getInventory().addItemDrop(9814, 1);
								player.getInventory().addItemDrop(9813, 1);
							});
							player.dialogue(npc.getId(), d -> {
								d.player(Mood.happy_plain_eyebrows_up, "Yeah I have that with me. Here you go.");
								d.npc(Mood.happy, "Wear the cape with pride, adventurer.");
							});
						} else {
							npc(Mood.sad, "I'm afraid you dont have enough money to buy this.");
						}
					}, "Sorry, nevermind.", () -> this.complete());
					
				} else {
					player(Mood.afraid, "I'm not sure. What can you do for me?");
					npc(Mood.happy, "I can offer you a quest cape "," once you reach maximum quest points.");
				}
			}
		});
	}
}