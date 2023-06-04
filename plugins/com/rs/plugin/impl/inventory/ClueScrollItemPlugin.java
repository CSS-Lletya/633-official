package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.trails.TreasureTrailsManager;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {7237, 7287, 7240, 19039}, itemNames = {  })
public class ClueScrollItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		int count = 0;
        for (int i : TreasureTrailsManager.REWARD_CASKETS) {
            if (i == item.getId()) {
                player.getInventory().deleteItem(item.getId(), 1);
                player.getTreasureTrailsManager().openReward(count);
                return;
            } else {
                count++;
                continue;
            }
        }
	}
}