package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;

import skills.SkillsDialogue;
import skills.cooking.Cooking;
import skills.cooking.CookingData;

public class CookingDialogue extends DialogueEventListener {

	private CookingData[] data;
	private GameObject object;
	private int itemUsed;

	public CookingDialogue(Player player, CookingData[] data, GameObject object, int itemUsed) {
		super(player);
		this.data = data;
		this.object = object;
		this.itemUsed = itemUsed;
	}

	@Override
	public void start() {
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.SELECT, 28, new int[] {itemUsed}, null, true);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		new Cooking(player, object, data[SkillsDialogue.getItemSlot(button)], true, SkillsDialogue.getQuantity(player)).start();
	}
}