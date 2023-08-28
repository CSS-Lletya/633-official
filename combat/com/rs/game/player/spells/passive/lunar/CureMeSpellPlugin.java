package com.rs.game.player.spells.passive.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.InterfaceVars;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 45, spellLevelRequirement = 71, spellbookId = 2, experience = 69)
public class CureMeSpellPlugin implements PassiveSpellListener {

	@Override
	public boolean canExecute(Player player) {
		if (!player.isPoisoned()) {
			player.getPackets().sendGameMessage("You are not poisoned.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getAudioManager().sendSound(Sounds.LUNAR_HEAL_ME);
		player.setNextAnimation(Animations.LUNAR_CURE_ME);
		player.setNextGraphics(Graphic.LUNAR_CURE_ME);
		player.getPoisonDamage().set(0);
		player.getVarsManager().sendVar(InterfaceVars.POISIONED_HP_ORB, 0);
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2), 
				new Item(ItemNames.COSMIC_RUNE_564, 2),
				new Item(ItemNames.LAW_RUNE_563, 1),
		};
	}
}