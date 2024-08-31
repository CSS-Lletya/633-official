package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.SkillsDialogue;
import skills.crafting.Loom;
import skills.crafting.Loom.LoomData;

@ObjectSignature(objectId = {}, name = { "Loom" })
public class LoomObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.dialogue(d -> {
			int[] ids = new int[LoomData.values().length];
			for (int i = 0; i < ids.length; i++)
				ids[i] = LoomData.values()[i].produced;
			d.skillsMenu((input) -> new Loom(player, LoomData.values()[SkillsDialogue.getItemSlot(input)], 28).start(), ids);
		});
	}
}