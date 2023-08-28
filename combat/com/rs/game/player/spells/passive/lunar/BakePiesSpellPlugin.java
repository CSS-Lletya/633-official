package com.rs.game.player.spells.passive.lunar;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

import skills.cooking.Cooking;
import skills.cooking.CookingData;

@PassiveSpellSignature(spellButton = 37, spellLevelRequirement = 65, spellbookId = 2, experience = 60)
public class BakePiesSpellPlugin implements PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (CookingData.VALUES.stream().noneMatch(
				raw -> ItemDefinitions.getItemDefinitions(raw.getRawId()).getName().toLowerCase().contains("pie")
						&& player.getInventory().containsAny(raw.getRawId()))) {
			player.getPackets().sendGameMessage("You need a pie in order to cast this spell.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		CookingData.VALUES.stream()
				.filter(raw -> player.getInventory().containsAny(raw.getRawId())
						&& ItemDefinitions.getItemDefinitions(raw.getRawId()).getName().toLowerCase().contains("pie"))
				.forEach(cookable -> {
					new Cooking(player, null, cookable, false, 1, true).start();
				});
	}

	@Override
	public Item[] runes() {
		return new Item[] { 
				new Item(ItemNames.ASTRAL_RUNE_9075, 2), 
				new Item(ItemNames.FIRE_RUNE_554, 5),
				new Item(ItemNames.WATER_RUNE_555, 4),
		};
	}
}