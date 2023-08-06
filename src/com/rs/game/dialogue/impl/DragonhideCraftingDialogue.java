package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;

import skills.SkillsDialogue;
import skills.crafting.DragonhideArmorCrafting;
import skills.crafting.DragonhideArmorCrafting.DragonHideData;

public class DragonhideCraftingDialogue extends DialogueEventListener {

	private DragonHideData data;

	public DragonhideCraftingDialogue(Player player, DragonHideData data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		DragonhideArmorCrafting fletching = new DragonhideArmorCrafting(player, data);
		player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).set(fletching);
		player.getAttributes().get(Attribute.DRAGONHIDE_FLETCHING).set(true);
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28,
				new int[] { fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId()},
				null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		DragonhideArmorCrafting.fletch(player, SkillsDialogue.getItemSlot(button));
	}
}