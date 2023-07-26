package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;

import skills.SkillsDialogue;
import skills.fletching.BowCarving;
import skills.fletching.BowCarving.Log;

public class BowFletchingDialogue extends DialogueEventListener {

	private Log data;

	public BowFletchingDialogue(Player player, Log data) {
		super(player);
		this.data = data;
	}

	@Override 
	public void start() {
		BowCarving fletching = new BowCarving(player, data, false);
		player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
		player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
		if (data == Log.MAGIC)
			SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28,
					new int[] { fletching.definition.producibles[0].producible.getId(),
							fletching.definition.producibles[1].producible.getId(),
							fletching.definition.producibles[2].producible.getId(),
							},
					null, true);
		 else
			SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28,
				
				new int[] { fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId(),
						(data == Log.MAGIC ? null : fletching.definition.producibles[3].producible.getId()),
						},
				null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
	}
}