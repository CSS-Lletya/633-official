package com.rs.plugin.listener;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;

/**
 * 
 * @author Dennis
 *
 */
public abstract class ObjectType {
	
	public void execute(Player player, GameObject object, int optionId) throws Exception {}

	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {}
}