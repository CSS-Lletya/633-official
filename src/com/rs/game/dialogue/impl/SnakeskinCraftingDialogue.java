package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.crafting.SnakeskinArmorCrafting;
import skills.crafting.SnakeskinArmorCrafting.SnakeData;

public class SnakeskinCraftingDialogue extends DialogueEventListener {

	private SnakeData[] data;

	public SnakeskinCraftingDialogue(Player player, SnakeData[] data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, new int[] {6322,6324,6326,6328,6330}, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new SnakeskinArmorCrafting(player, data[SkillsDialogue.getItemSlot(button)]).start();
	}
}