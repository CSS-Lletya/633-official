package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.crafting.MoltenGlassBlowing;
import skills.crafting.MoltenGlassBlowing.GlassData;

public class MoltenGlassDialgoue extends DialogueEventListener {

	private GlassData[] data;

	public MoltenGlassDialgoue(Player player, GlassData[] data) {
		super(player);
		this.data = data;
	}

	@Override
	public void start() {
		int[] ids = new int[GlassData.values().length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = GlassData.values()[i].item;
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, ids, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new MoltenGlassBlowing(player, data[SkillsDialogue.getItemSlot(button)], 28).start();
	}
}