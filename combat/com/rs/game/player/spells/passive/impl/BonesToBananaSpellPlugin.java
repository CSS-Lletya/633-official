package com.rs.game.player.spells.passive.impl;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpell;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

/**
 * This is just a demo test, not completed.
 * @author Dennis
 *
 */
@PassiveSpellSignature(spellButton = 33, spellLevelRequirement = 15)
public class BonesToBananaSpellPlugin implements PassiveSpell {

	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		System.out.println("SUP");
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.EARTH_RUNE_557, 2),
				new Item(ItemNames.WATER_RUNE_555, 2),
				new Item(ItemNames.NATURE_RUNE_561, 1)
		};
	}
}