package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.crafting.PotteryWheel;
import skills.crafting.PotteryWheel.PotteryWheelData;

public class PotteryWheelD extends DialogueEventListener {

	private PotteryWheelData[] data;

	public PotteryWheelD(Player player, PotteryWheelData[] data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		int[] ids = new int[PotteryWheelData.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = PotteryWheelData.values()[i].produced;
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, ids, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new PotteryWheel(player, data[SkillsDialogue.getItemSlot(button)], 28).start();
	}
}