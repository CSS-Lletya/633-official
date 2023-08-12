package com.rs.plugin.impl.objects.region;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.thieving.impl.WallSafes;

@ObjectSignature(objectId = {}, name = {"Wall safe"})
public class RougesDenRegionObjectPlugin extends ObjectListener{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		new WallSafes(player).start();
	}
}