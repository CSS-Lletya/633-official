package com.rs.plugin.impl.objects;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.SkillDialogueFeedback;

import skills.SkillsDialogue;
import skills.crafting.SpinningWheel;
import skills.crafting.SpinningWheel.SpinningData;

@ObjectSignature(objectId = {}, name = { "Spinning wheel" })
public class SpinningWheelObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.dialogue(d -> {
			int[] ids = new int[SpinningData.values().length];
			for (int i = 0; i < ids.length; i++)
				ids[i] = SpinningData.values()[i].item.getId();
			d.skillsMenu(ids);
			d.skillDialogue(new SkillDialogueFeedback() {
				@Override
				public void handle(int button) {
					new SpinningWheel(player, SpinningData.values()[SkillsDialogue.getItemSlot(button)], 28).start();
				}
			});
		});
	}

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		player.dialogue(d -> {
			int[] ids = new int[SpinningData.values().length];
			for (int i = 0; i < ids.length; i++)
				ids[i] = SpinningData.values()[i].item.getId();
			d.skillsMenu(ids);
			d.skillDialogue(new SkillDialogueFeedback() {
				@Override
				public void handle(int button) {
					new SpinningWheel(player, SpinningData.values()[SkillsDialogue.getItemSlot(button)], 28).start();
				}
			});
		});
	}
}