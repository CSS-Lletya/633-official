package com.rs.plugin.impl.objects;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.actions.SandBucketFillAction;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Sandpit" })
public class SandPitObjectPlugin extends ObjectListener {

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == 1925) {
			player.getAction().setAction(new SandBucketFillAction());
		}
	}
}