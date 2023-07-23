package com.rs.plugin.impl.npcs.region;

import com.rs.GameConstants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.Skills;
import skills.summoning.EnchantedHeadwear.Headwear;

@NPCSignature(name = {}, npcId = { 6970 })
public class TaverlyRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (npc.getId() == 6970) {
			if (option == 3) {
				player.dialogue(6970, d -> d.npc(Mood.happy, "Show me the item you wish to enchant!"));
			} else
				player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT);
		}
	}

	@Override
	public void executeItemOnNPC(Player player, NPC npc, Item itemUsed) {
		if (npc.getId() == 6970) {
			Headwear.VALUES.stream().filter(enchantable -> enchantable.baseItem == itemUsed.getId())
			.forEach(helm -> {
				if (helm != null) {
					if (helm.baseItem == helm.enchantedItem) {
						player.getPackets().sendGameMessage(
								"This headwear is already resonating with familiar magic. There is no need to enchant it.");
						return;
					}
					if (player.getSkills().getLevel(Skills.SUMMONING) >= helm.summoningRequirement) {
						if (itemUsed.getId() == helm.baseItem) {
							player.getInventory().deleteItem(itemUsed.getSlot(), itemUsed);
							player.getInventory().addItem(helm.enchantedItem, 1);
							player.dialogue(6970, d -> d.npc(Mood.happy, "There you are! I have just enchanted ","your " + ItemDefinitions.getItemDefinitions(helm.baseItem).getName() + " for you."));
							player.getPackets().sendGameMessage("Pikkupstix magically enchants your headwear.");
							player.getDetails().getStatistics().addStatistic("Summoning_Items_Enchanted");
							return;
						}

						if (itemUsed.getId() == helm.enchantedItem) {
							player.getInventory().deleteItem(itemUsed.getSlot(), itemUsed);
							player.getInventory().addItem(helm.baseItem, 1);
							player.getPackets().sendGameMessage("Pikkupstix removes the enchantment from your headwear.");
							return;
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a summoning level of " + helm.summoningRequirement + " to enchant this.");
					}
				}
			});
		}
	}
}