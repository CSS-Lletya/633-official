package com.rs.plugin.impl.inventory;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Appearance;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.RandomUtility;

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
				transformInto(player, 3689 + RandomUtility.random(5));
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
		player.setNextAnimation(Animations.TRANSFORMING_RING_DEACTIVATION);
		player.getAppearance().transformIntoNPC(Appearance.RESET_AS_NPC);
		player.getInterfaceManager().removeInventoryInterface();
	}
}