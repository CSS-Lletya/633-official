package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.content.WildernessObelisk;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Obelisk"})
public class WildernessObiliskObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		WildernessObelisk.activateObelisk(object.getId(), player);
	}
}