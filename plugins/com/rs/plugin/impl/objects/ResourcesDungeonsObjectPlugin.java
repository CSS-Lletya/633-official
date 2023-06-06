package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.dungeoneering.ResourceDungeons;

@ObjectSignature(objectId = {}, name = {"Mysterious Entrance", "Mysterious Door"})
public class ResourcesDungeonsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		ResourceDungeons.handleDungeons(player, object);
	}
}