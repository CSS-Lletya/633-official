package com.rs.plugin.impl.inventory;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.magic.Magic;

@InventoryWrapper(itemId = {4251}, itemNames = {  })
public class EctophialItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		player.getInventory().deleteItem(item);
		player.setNextGraphics(Graphic.ECTOPHIAL_LIQUID);
		player.setNextAnimation(Animations.EMPTY_ECTOPHIAL);
		World.get().submit(new Task(6) {
			@Override
			protected void execute() {
				Magic.sendTeleportSpell(player, 8939, 8941, 1678, 1679, 0, 0, new WorldTile(3662, 3518, 0), 4, true, 1);
				this.cancel();
			}
		});
	}
}