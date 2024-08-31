package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.SkillsDialogue;
import skills.crafting.PotteryOven;
import skills.crafting.PotteryOven.PotteryData;

@ObjectSignature(objectId = {}, name = {"Pottery oven"})
public class PotteryOvenObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.faceObject(object);
		player.dialogue(d -> {
			int[] ids = new int[PotteryData.values().length];
			for (int i = 0; i < ids.length; i++)
				ids[i] = PotteryData.values()[i].produced;
			d.skillsMenu((input) -> new PotteryOven(player, PotteryData.values()[SkillsDialogue.getItemSlot(input)], 28).start(), ids);
		});
	}
}