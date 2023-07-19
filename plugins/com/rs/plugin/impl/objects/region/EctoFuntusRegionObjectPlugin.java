package com.rs.plugin.impl.objects.region;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.prayer.EctoFuntus;

@ObjectSignature(objectId = {5281,37454, 11162, 5267, 5278, 5268, 5264, 9308, 9307, 5263, 5262, 5282, 11163, 11164, 17119 }, name = {})
public class EctoFuntusRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		EctoFuntus.handleObjects(player, object);
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		EctoFuntus.handleItemOnObject(player, item.getId(), object.getId());
	}
}