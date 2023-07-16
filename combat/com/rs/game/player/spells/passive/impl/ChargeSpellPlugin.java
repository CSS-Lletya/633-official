package com.rs.game.player.spells.passive.impl;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

/**
 * TODO: Combat relations: https://oldschool.runescape.wiki/w/Charge
 * @author Dennis
 *
 */
@PassiveSpellSignature(spellButton = 83, spellLevelRequirement = 80)
public class ChargeSpellPlugin implements PassiveSpellListener {

	@Override
	public boolean canExecute(Player player) {
		if (!player.getDetails().getChargeDelay().finished()) {
			player.getPackets().sendGameMessage("You need to wait for the spell to recharge.");
			return false;
		}
		if ((player.getEquipment().getWeaponId() == 2415 && player.getEquipment().getCapeId() == 2412)
                || (player.getEquipment().getWeaponId() == 2416 && player.getEquipment().getCapeId() == 2413)
                || (player.getEquipment().getWeaponId() == 2417 && player.getEquipment().getCapeId() == 2414)) {
			
			return true;
		} else {
			player.getPackets().sendGameMessage("You do not have the required equipment to cast this spell.");
			return false;
		}
	}

	@Override
	public void execute(Player player) {
		player.getDetails().getChargeDelay().start(60);
		player.setNextAnimation(Animations.CHARGE_SPELL);
		player.setNextGraphics(Graphic.CHARGE_SPELL);
		player.getAudioManager().sendSound(Sounds.CHARGE_SPELL);
		player.getPackets().sendGameMessage("You feel charged with magic power.");
		player.getDetails().getStatistics().addStatistic("Charges_Performed");
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.FIRE_RUNE_554, 3),
				new Item(ItemNames.BLOOD_RUNE_565, 3),
				new Item(ItemNames.AIR_RUNE_556, 3)
		};
	}
}