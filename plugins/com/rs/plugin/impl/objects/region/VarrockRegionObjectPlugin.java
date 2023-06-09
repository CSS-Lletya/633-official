package com.rs.plugin.impl.objects.region;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {46034}, name = {})
public class VarrockRegionObjectPlugin extends ObjectListener{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getId() == 46034) {
			if (player.getInventory().addItem(new Item(15287))) {
				player.getPackets().sendGameMessage("You take a leaflet from the table.");
				return;
			}
		}
	}
}