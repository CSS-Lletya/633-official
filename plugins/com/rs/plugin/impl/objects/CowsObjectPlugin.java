package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.Skills;

@ObjectSignature(objectId = {12111}, name = { "Dairy cow" })
public class CowsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (player.getMovement().isLocked())
			return;
		if (object.getId() == 12111) {
			if (optionId == 2) {
				if (player.getInventory().containsAny(1925)) {
					player.getMovement().lock(8);
					player.getAudioManager().sendSound(Sounds.COW_INTERACT);
					player.setNextAnimation(Animations.COW_MILKING);
					player.getInventory().replaceItems(new Item(1925), new Item(1927));
					player.getPackets().sendGameMessage("You milk the cow.");
				} else
					player.getPackets().sendGameMessage("You need an empty bucket in order to milk this cow properly.");
			}
			if (optionId == 3) {
				if (player.getSkills().getLevel(Skills.THIEVING) < 15) {
					player.getPackets().sendGameMessage("You need a thieving level of at least 15 to do this.");
					return;
				}
				if (player.getInventory().addItem(new Item(10593))) {
					player.getMovement().lock(4);
					player.setNextAnimation(Animations.TWO_HANDED_GRAB);
					player.getSkills().addExperience(Skills.THIEVING, 16);
					player.getPackets().sendGameMessage("The cow looks at you angrily..");
				}
			}
			return;
		}
		if (optionId == 1) {
			if (player.getInventory().containsAny(1925)) {
				player.getMovement().lock(8);
				player.setNextAnimation(Animations.COW_MILKING);
				player.getInventory().replaceItems(new Item(1925), new Item(1927));
				player.getPackets().sendGameMessage("You milk the cow.");
			} else
				player.getPackets().sendGameMessage("You need an empty bucket in order to milk this cow properly.");
		}
		if (optionId == 2) {
			if (player.getSkills().getLevel(Skills.THIEVING) < 15) {
				player.getPackets().sendGameMessage("You need a thieving level of at least 15 to do this.");
				return;
			}
			if (player.getInventory().addItem(new Item(10593))) {
				player.getMovement().lock(4);
				player.setNextAnimation(Animations.TWO_HANDED_GRAB);
				player.getSkills().addExperience(Skills.THIEVING, 16);
				player.getPackets().sendGameMessage("The cow looks at you angrily..");
			}
		}
	}
}