package com.rs.game.player.spells.passive.lunar;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

import skills.crafting.AmuletStringing;
import skills.crafting.AmuletStringing.AmuletData;

@PassiveSpellSignature(spellButton = 44, spellLevelRequirement = 80, spellbookId = 2, experience = 87)
public class StringJewelrySpellPlugin implements PassiveSpellListener {

	@Override
	public boolean canExecute(Player player) {
		if (AmuletData.VALUES.stream()
				.noneMatch(unstrung -> player.getInventory().containsAny(unstrung.item.getId()))) {
			player.getPackets().sendGameMessage("You need jewellery in order to use this spell.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		AmuletData.VALUES.stream().filter(unstrung -> player.getInventory().containsAny(unstrung.item.getId()))
				.forEach(unstrung -> new AmuletStringing(player, unstrung, true).start());
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