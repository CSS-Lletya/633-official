package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.thieving.chest.ChestThieving;

@ObjectSignature(objectId = { 2567, 2568, 2566, 2573, 2569, 2570}, name = {})
public class ThievableChestsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.faceObject(object);
		ChestThieving.handleChest(player, object, (optionId != 1));
	}
}