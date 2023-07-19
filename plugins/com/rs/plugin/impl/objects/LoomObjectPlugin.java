package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.impl.LoomDialogue;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.crafting.Loom.LoomData;

@ObjectSignature(objectId = {}, name = {"Loom"})
public class LoomObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.dialogueBlank(new LoomDialogue(player, LoomData.values()));
	}
}