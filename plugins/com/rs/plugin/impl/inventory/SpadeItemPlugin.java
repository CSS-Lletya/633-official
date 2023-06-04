package com.rs.plugin.impl.inventory;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = { 952 }, itemNames = {})
public class SpadeItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (option == 1) {
			player.resetWalkSteps();
			player.setNextAnimation(Animations.SPADE_DIG);
			player.getMovement().lock();
			player.task(1, p -> {
				player.getMovement().unlock();
				if (player.getTreasureTrailsManager().useDig())
					return;
				player.getPackets().sendGameMessage("You find nothing.");
			});
		}
	}
}