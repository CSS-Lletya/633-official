package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction;
import com.rs.game.player.actions.FillAction.Filler;

import skills.SkillsDialogue;

public class ItemFillingDialogue extends DialogueEventListener {

	private Filler filler;

	public ItemFillingDialogue(Player player, Filler filler) {
		super(player);
		this.filler = filler;
	}

	@Override
	public void start() {
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, new int[] {filler.getFilled().getId()}, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		player.getAction().setAction(new FillAction(SkillsDialogue.getQuantity(player)));
	}
}