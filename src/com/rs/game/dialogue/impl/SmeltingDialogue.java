package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.smithing.Smelting;
import skills.smithing.Smelting.SmeltingData;

public class SmeltingDialogue extends DialogueEventListener {

	private SmeltingData[] data;

	public SmeltingDialogue(Player player, SmeltingData[] data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, new int[] {2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363}, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new Smelting(player, data[SkillsDialogue.getItemSlot(button)], 28, false).start();
	}
}