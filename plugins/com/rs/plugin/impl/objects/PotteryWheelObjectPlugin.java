package com.rs.plugin.impl.objects;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.SkillDialogueFeedback;

import skills.SkillsDialogue;
import skills.crafting.PotteryWheel;
import skills.crafting.PotteryWheel.PotteryWheelData;

@ObjectSignature(objectId = {}, name = { "Potter's Wheel" })
public class PotteryWheelObjectPlugin extends ObjectListener {
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == 1761) {
			player.faceObject(object);
			player.dialogue(d -> {
				int[] ids = new int[PotteryWheelData.values().length];
				for (int i = 0; i < ids.length; i++)
					ids[i] = PotteryWheelData.values()[i].produced;
				d.skillsMenu(ids);
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						new PotteryWheel(player, PotteryWheelData.values()[SkillsDialogue.getItemSlot(button)], 28).start();
					}
				});
			});
		}
	}
}