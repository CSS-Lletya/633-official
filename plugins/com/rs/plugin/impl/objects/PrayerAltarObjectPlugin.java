package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.Skills;

@ObjectSignature(objectId = {}, name = {"Altar"})
public class PrayerAltarObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getDefinitions().containsOption("Pray")){
			final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
            if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                player.setNextAnimation(Animations.PRAYING_TO_ALTAR);
                player.getPrayer().restorePrayer(maxPrayer);
                player.getPackets().sendGameMessage("You've recharged your prayer points.");
                return;
            } else {
                player.getPackets().sendGameMessage("You already have full Prayer points.");
                return;
            }
		}
	}
}