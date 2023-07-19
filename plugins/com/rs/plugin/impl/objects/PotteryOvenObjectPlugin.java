package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.impl.PotteryOvenDialogue;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.crafting.PotteryOven.PotteryData;

@ObjectSignature(objectId = {}, name = {"Pottery oven"})
public class PotteryOvenObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.faceObject(object);
		player.dialogueBlank(new PotteryOvenDialogue(player, PotteryData.values()));
	}
}