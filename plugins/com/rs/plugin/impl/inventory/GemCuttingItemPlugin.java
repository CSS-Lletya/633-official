package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.CrabShellCrafting;
import skills.crafting.GemCutting;
import skills.crafting.LimestoneBrickCrafting;
import skills.crafting.SnailHelmCrafting;

@InventoryWrapper(itemId = { 1755,
		//gems
		1625, 1627, 1629, 1623, 1621, 1619, 1617, 1631, 6571, 1609, 1611, 411, 413, 1613,
		1607, 1605, 1603, 1601, 1615, 6573, 9953, 10107,
		//crab
		ItemNames.FRESH_CRAB_SHELL_7538, ItemNames.FRESH_CRAB_CLAW_7536,
		//snelms
		3349, 3347, 3345, 3353, 3351,
		//limestones
		3211
}, itemNames = {})
public class GemCuttingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		GemCutting.cut(player, firstItem, secondItem);
		
		new UseWith(new Item(1755), new Item(ItemNames.FRESH_CRAB_SHELL_7538)).execute(firstItem, secondItem, () -> {
			new CrabShellCrafting(player, new Item(ItemNames.FRESH_CRAB_SHELL_7538)).start();
		});
		new UseWith(new Item(1755), new Item(ItemNames.FRESH_CRAB_CLAW_7536)).execute(firstItem, secondItem, () -> {
			new CrabShellCrafting(player, new Item(ItemNames.FRESH_CRAB_CLAW_7536)).start();
		});
		new UseWith(new Item(1755), new Item(ItemNames.LIMESTONE_3211)).execute(firstItem, secondItem, () -> {
			System.out.println("??");
			new LimestoneBrickCrafting(player).start();
		});
		SnailHelmCrafting.create(player, firstItem, secondItem);
	}
}