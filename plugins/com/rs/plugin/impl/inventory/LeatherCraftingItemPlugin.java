package com.rs.plugin.impl.inventory;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.SkillDialogueFeedback;

import skills.SkillsDialogue;
import skills.crafting.DragonhideArmorCrafting;
import skills.crafting.DragonhideArmorCrafting.DragonHideData;
import skills.crafting.HardLeatherCrafting;
import skills.crafting.SnakeskinArmorCrafting;
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
			player.dialogue(d -> {
				d.skillsMenu(6322,6324,6326,6328,6330);
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						new SnakeskinArmorCrafting(player, SnakeData.values()[SkillsDialogue.getItemSlot(button)]).start();
					}
				});
			});
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(1745)).execute(firstItem, secondItem, () -> {
			player.dialogue(d -> {
				DragonhideArmorCrafting fletching = new DragonhideArmorCrafting(player, DragonHideData.GREEN);
				player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).set(fletching);
				player.getAttributes().get(Attribute.DRAGONHIDE_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						DragonhideArmorCrafting.craft(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(2505)).execute(firstItem, secondItem, () -> {
			player.dialogue(d -> {
				DragonhideArmorCrafting fletching = new DragonhideArmorCrafting(player, DragonHideData.BLUE);
				player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).set(fletching);
				player.getAttributes().get(Attribute.DRAGONHIDE_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						System.out.println(fletching.definition.producibles[0].hideRequired);
						DragonhideArmorCrafting.craft(player, SkillsDialogue.getItemSlot(button));
					}
				}); 
			});
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(2507)).execute(firstItem, secondItem, () -> {
			player.dialogue(d -> {
				DragonhideArmorCrafting fletching = new DragonhideArmorCrafting(player, DragonHideData.RED);
				player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).set(fletching);
				player.getAttributes().get(Attribute.DRAGONHIDE_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						DragonhideArmorCrafting.craft(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		new UseWith(new Item(ItemNames.NEEDLE_1733), new Item(2509)).execute(firstItem, secondItem, () -> {
			player.dialogue(d -> {
				DragonhideArmorCrafting fletching = new DragonhideArmorCrafting(player, DragonHideData.BLACK);
				player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).set(fletching);
				player.getAttributes().get(Attribute.DRAGONHIDE_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						DragonhideArmorCrafting.craft(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
	}
}