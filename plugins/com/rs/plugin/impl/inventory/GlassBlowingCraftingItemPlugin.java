package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.SkillsDialogue;
import skills.crafting.MoltenGlassBlowing;
import skills.crafting.MoltenGlassBlowing.GlassData;

@InventoryWrapper(itemId = { ItemNames.GLASSBLOWING_PIPE_1785, ItemNames.MOLTEN_GLASS_1775 }, itemNames = {})
public class GlassBlowingCraftingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		player.dialogue(d -> {
			int[] ids = new int[GlassData.values().length];
			for (int i = 0; i < ids.length; i++)
				ids[i] = GlassData.values()[i].item;
			d.skillsMenu((input) -> new MoltenGlassBlowing(player, GlassData.values()[SkillsDialogue.getItemSlot(input)], 28).start(), ids);
		});
	}
}