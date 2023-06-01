package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.SkillsDialogue;

import skills.cooking.DoughCreation;
import skills.cooking.DoughCreation.DoughData;

public class DoughCreatingD extends DialogueEventListener {

	private DoughData[] data;
	private Item used, onto;

	public DoughCreatingD(Player player, DoughData[] data, Item used, Item onto) {
		super(player);
		this.data = data;
		this.used = used;
		this.onto = onto;
	}

	@Override
	public void start() {
		if (!DoughCreation.getDefinition(used.getId(), onto.getId()).isPresent())
			return;
		int[] ids = new int[data.length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = DoughData.values()[i].produced.getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, 28, ids, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new DoughCreation(player, data[SkillsDialogue.getItemSlot(button)], used,onto).start();
	}
}