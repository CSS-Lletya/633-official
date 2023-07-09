package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;

import skills.SkillsDialogue;
import skills.fletching.BowCarving;
import skills.fletching.BowCarving.Log;

public class BowFletchingD extends DialogueEventListener {

	private Log[] data;

	public BowFletchingD(Player player, Log[] data2) {
		super(player);
	}
	

	@Override
	public void start() {
		for (Log log : Log.values()) {
			BowCarving fletching = new BowCarving(player, log, false, 28);
			player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
			player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
			SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, new int[] {
					fletching.definition.producibles[0].producible.getId(),
					fletching.definition.producibles[1].producible.getId(),
					fletching.definition.producibles[2].producible.getId(),
					fletching.definition.producibles[3].producible.getId(),
				}, null, true);
		}
	}

	@Override
	public void listenToDialogueEvent(int button) {
		System.out.println(SkillsDialogue.getItemSlot(button));
		new BowCarving(player, data[SkillsDialogue.getItemSlot(button)], false, SkillsDialogue.getQuantity(player)).start();
//			BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
	}
}