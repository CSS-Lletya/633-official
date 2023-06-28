package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.content.traveling.SpiritTree;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {26723}, name = {"Spirit Tree"})
public class SpiritTreeObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		SpiritTree.openInterface(player, false);
	}
}