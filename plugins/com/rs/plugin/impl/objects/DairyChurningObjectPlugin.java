package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.cooking.DairyChurning;

@ObjectSignature(objectId = {}, name = {"Dairy churn"})
public class DairyChurningObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		DairyChurning.handleChurnOption(player);
	}
}