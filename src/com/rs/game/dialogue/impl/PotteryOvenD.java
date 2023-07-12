package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.crafting.PotteryOven;
import skills.crafting.PotteryOven.PotteryData;

public class PotteryOvenD extends DialogueEventListener {

	private PotteryData[] data;

	public PotteryOvenD(Player player, PotteryData[] data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		int[] ids = new int[PotteryData.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = PotteryData.values()[i].produced;
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, ids, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new PotteryOven(player, data[SkillsDialogue.getItemSlot(button)], 28).start();
	}
}