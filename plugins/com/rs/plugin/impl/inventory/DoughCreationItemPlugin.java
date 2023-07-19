package com.rs.plugin.impl.inventory;

import com.rs.game.dialogue.impl.DoughCreatingDialogue;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.cooking.DoughCreation.DoughData;

@InventoryWrapper(itemId = {1933}, itemNames = {  })
public class DoughCreationItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		player.dialogueBlank(new DoughCreatingDialogue(player, DoughData.values(), firstItem, secondItem));
	}
}