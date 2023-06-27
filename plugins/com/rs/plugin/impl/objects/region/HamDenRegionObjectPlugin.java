package com.rs.plugin.impl.objects.region;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.TeleportType;

@ObjectSignature(objectId = {5493}, name = {})
public class HamDenRegionObjectPlugin extends ObjectListener{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.getMovement().move(false, new WorldTile(3165,3251, 0), TeleportType.LADDER);
	}
}