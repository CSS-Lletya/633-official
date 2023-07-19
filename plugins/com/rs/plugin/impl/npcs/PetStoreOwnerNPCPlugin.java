package com.rs.plugin.impl.npcs;

import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.IntegerInputAction;
import com.rs.utilities.Utility;

@NPCSignature(name = { "Pet shop owner" }, npcId = {})
public class PetStoreOwnerNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		switch (option) {
		case 1:
			player.getInterfaceManager().sendChatBoxInterface(668);
			break;
		case 3:
			player.getPackets().sendInputIntegerScript(
					"How many will you sell? (25 coins each, you have "
							+ Utility.getFormattedNumber(player.getInventory().getAmountOf(12183)) + ")",
					new IntegerInputAction() {
						@Override
						public void handle(int input) {
							int invAmount = player.getInventory().getAmountOf(12183);
							if (input > invAmount)
								input = invAmount;
							for (int i = 0; i < input; i++)
								player.getDetails().getStatistics().addStatistic("Spirit_Shards_Traded");
							player.getInventory().deleteItem(new Item(12183, input));
							player.getInventory().addItem(new Item(995, input * 25));
							player.getPackets().sendGameMessage("You sell " + Utility.getFormattedNumber(player.getInventory().getAmountOf(12183)) + " spirit shards for " + Utility.getFormattedNumber((input * 25)) + " coins.");
						}
					});
			break;
		}
	}
}