package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.dialogue.impl.MoltenGlassDialgoue;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.MoltenGlassBlowing.GlassData;

@InventoryWrapper(itemId = {ItemNames.GLASSBLOWING_PIPE_1785, ItemNames.MOLTEN_GLASS_1775}, itemNames = {})
public class GlassBlowingCraftingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		player.dialogueBlank(new MoltenGlassDialgoue(player, GlassData.values()));
	}
}