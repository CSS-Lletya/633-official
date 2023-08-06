package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.dialogue.impl.DragonhideCraftingDialogue;
import com.rs.game.dialogue.impl.SnakeskinCraftingDialogue;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.crafting.DragonhideArmorCrafting.DragonHideData;
import skills.crafting.HardLeatherCrafting;
import skills.crafting.SnakeskinArmorCrafting.SnakeData;

@InventoryWrapper(itemId = { ItemNames.NEEDLE_1733, ItemNames.THREAD_1734, ItemNames.LEATHER_1741,
		ItemNames.HARD_LEATHER_1743, ItemNames.SNAKESKIN_6289, 1745, 2505, 2507, 2509 }, itemNames = {})
public class LeatherCraftingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if (!player.getInventory().containsAny(ItemNames.THREAD_1734)) {
			player.getPackets().sendGameMessage("You need thread to make this.");
			return;
		}
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(ItemNames.LEATHER_1741)).execute(firstItem, secondItem, () -> {
			player.getInterfaceManager().sendInterface(154);
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(ItemNames.HARD_LEATHER_1743)).execute(firstItem, secondItem, () -> {
			new HardLeatherCrafting(player).start();
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(ItemNames.SNAKESKIN_6289)).execute(firstItem, secondItem, () -> {
			player.dialogueBlank(new SnakeskinCraftingDialogue(player, SnakeData.values()));
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(1745)).execute(firstItem, secondItem, () -> {
			player.dialogueBlank(new DragonhideCraftingDialogue(player, DragonHideData.GREEN));
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(2505)).execute(firstItem, secondItem, () -> {
			player.dialogueBlank(new DragonhideCraftingDialogue(player, DragonHideData.BLUE));
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(2507)).execute(firstItem, secondItem, () -> {
			player.dialogueBlank(new DragonhideCraftingDialogue(player, DragonHideData.RED));
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(2509)).execute(firstItem, secondItem, () -> {
			player.dialogueBlank(new DragonhideCraftingDialogue(player, DragonHideData.BLACK));
		});
	}
}