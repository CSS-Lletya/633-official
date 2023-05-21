package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.runecrafting.OuraniaAltar;

@ObjectSignature(objectId = {26847}, name = {})
public class OuraniaAltarObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		OuraniaAltar.craftRune(player);
	}
}