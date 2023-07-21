package com.rs.plugin.impl.inventory;

import com.rs.content.mapzone.impl.SorceresssGardenMapZone;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {14057}, itemNames = {  })
public class SorceresssGardenBroomstickItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (option == 1) {
			player.setNextAnimation(new Animation(10532));
			player.setNextGraphics(new Graphics(1866));
		}
		if (option == 6)
			SorceresssGardenMapZone.teleportToSocreressGarden(player, true);
	}
}