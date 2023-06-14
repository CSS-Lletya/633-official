package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.RandomUtils;

@InventoryWrapper(itemId = { 6583 }, itemNames = { "Ring of stone", "Easter ring" })
public class MorphingRingsItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		if (option == 2) {
			switch (item.getName()) {
			case "Ring of stone":
				transformInto(player, 2626);
				break;
			case "Easter ring":
				transformInto(player, 3689 + RandomUtils.random(5));
				break;
			}
		}
	}

	public static void transformInto(Player player, int npcId) {
		player.getMovement().stopAll(true, true, true);
		player.getMovement().lock();
		player.getAppearance().transformIntoNPC(npcId);
		player.getInterfaceManager().sendInventoryInterface(375);
	}

	public static void deactivateTransformation(Player player) {
		player.getMovement().unlock();
		player.setNextAnimation(new Animation(14884));
		player.getAppearance().transformIntoNPC(-1);
		player.getInterfaceManager().removeInventoryInterface();
	}
}