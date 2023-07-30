package com.rs.plugin.impl.objects;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.smithing.StudCreation;

@ObjectSignature(objectId = {}, name = {"Anvil"})
public class AnvilObjectPlugin extends ObjectListener {

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == ItemNames.STEEL_BAR_2353) {
			new StudCreation(player).start();
		}
	}
}