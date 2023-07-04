package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.magic.TeleportType;

@InventoryWrapper(itemId = { 9007, 9008, 9009, 9013, 9010, 9011, 9012 }, itemNames = {})
public class SkullSceptreItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		if (option == 3) {
			player.getDetails().getSkullSceptreCharges().getAndDecrement(1);
			if (player.getDetails().getSkullSceptreCharges().get() < 1) {
				player.getInventory().deleteItem(item);
				player.getPackets().sendGameMessage("Your staff crumbles to dust as you use its last charge.");
			}
			player.getMovement().move(false, new WorldTile(3081, 3421, 0), TeleportType.NORMAL);
		}
		else if (option == 6) {
			if (player.getDetails().getSkullSceptreCharges().get() < 1) {
				player.getPackets().sendGameMessage("You don't have enough charges left.");
				return;
			}
			player.getPackets().sendGameMessage("Concentrating deeply, you divine that the sceptre has " + (player.getDetails().getSkullSceptreCharges().get()) + " charges left.");
		}
			
	}

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		new UseWith(new Item(9010), new Item(9011)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(9010));
			player.getInventory().replaceItems(new Item(9011), new Item(9012));
			player.dialogue(d -> d.item(9009, "The two halves of the skull fit perfectly."));
		});
		new UseWith(new Item(9007), new Item(9008)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(9007));
			player.getInventory().replaceItems(new Item(9008), new Item(9009));
			player.dialogue(d -> d.item(9012, "The two halves of the sceptre fit perfectly."));
		});
		new UseWith(new Item(9012), new Item(9009)).execute(firstItem, secondItem, () -> {
			player.getInventory().deleteItem(new Item(9012));
			player.getInventory().replaceItems(new Item(9009), new Item(9013));
			player.getDetails().getSkullSceptreCharges().set(10);//diary affects this
			player.dialogue(d -> d.item(9013, "The skull fits perfectly atop the Sceptre."));
		});
	}
}