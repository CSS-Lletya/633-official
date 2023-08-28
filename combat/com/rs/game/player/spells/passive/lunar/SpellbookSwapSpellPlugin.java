package com.rs.game.player.spells.passive.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 34, spellLevelRequirement = 96, spellbookId = 2, experience = 130)
public class SpellbookSwapSpellPlugin implements PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(9);
		player.getDetails().getComponentLockTimer().start(7);
		player.setNextGraphics(Graphic.SPELLBOOK_SWAP);
		player.setNextAnimation(Animations.SPELLBOOK_SWAP);
		player.getAudioManager().sendSound(Sounds.LUNAR_CHANGE_SPELLBOOK);
		player.getCombatDefinitions().setAutoCastSpell(0);
		player.dialogue(d -> {
			d.option(
					"Modern", () -> player.getCombatDefinitions().setSpellBook(0),
					"Ancient", () -> player.getCombatDefinitions().setSpellBook(1),
					"Nevermind", d::complete);
		});
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 3),
				new Item(ItemNames.COSMIC_RUNE_564, 2),
				new Item(ItemNames.LAW_RUNE_563, 1)
		};
	}
}