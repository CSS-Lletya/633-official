package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Small table", "Large table", "counter", "table" })
public class ItemPlacingObjetPlugin extends ObjectListener {

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		player.setNextAnimation(Animations.SIMPLE_GRAB);
        player.getInventory().deleteItem(item);
        FloorItem.addGroundItem(item, object, player, false);
	}
}