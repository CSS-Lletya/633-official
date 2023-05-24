package com.rs.plugin.impl.objects.region;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.mining.Mining;
import skills.mining.RockData;

@ObjectSignature(objectId = {}, name = {"Mineral deposit"})
public class LivingRockCavernsRegionNPCPlugin extends ObjectType{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		int id = object.getId();
		new Mining(player, (id == 5999 ? RockData.LRC_COAL : RockData.LRC_GOLD), object).start();
	}
}