package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.impl.SpinningDialogue;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.crafting.SpinningWheel.SpinningData;

@ObjectSignature(objectId = {}, name = { "Spinning wheel" })
public class SpinningWheelObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.dialogueBlank(new SpinningDialogue(player, SpinningData.values()));
	}

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		player.dialogueBlank(new SpinningDialogue(player, SpinningData.values()));
	}
}