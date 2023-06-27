package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.crafting.Loom;
import skills.crafting.Loom.LoomData;

public class LoomD extends DialogueEventListener {

	private LoomData[] data;

	public LoomD(Player player, LoomData[] data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		int[] ids = new int[LoomData.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = LoomData.values()[i].produced;
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, ids, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new Loom(player, data[SkillsDialogue.getItemSlot(button)], 28).start();
	}
}