package com.rs.net.packets.outgoing.impl;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction.Filler;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.ItemCombine;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.SkillDialogueFeedback;
import com.rs.utilities.Utility;

import skills.SkillsDialogue;
import skills.cooking.DoughCreation;
import skills.cooking.FoodSlicing;
import skills.crafting.SoftClayCreation;
import skills.firemaking.Firemaking;
import skills.fletching.BowCarving;
import skills.fletching.BowCarving.Log;
import skills.magic.spells.PassiveSpellDispatcher;
import skills.magic.spells.modern.enchanting.Enchanting;

@OutgoingPacketSignature(packetId = 33, description = "Represents an Interface being used on another Interface")
public class InterfaceOnInterfacePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		final int toInterfaceBitmap = stream.readInt();
		int toSlotId = stream.readUnsignedShortLE128();
		int toItemId = stream.readUnsignedShort();
		final int fromInterfaceBitmap = stream.readInt();
		int fromItemId = stream.readUnsignedShortLE128();
		int fromSlotId = stream.readUnsignedShort128();

		final int fromInterfaceId = fromInterfaceBitmap >> 16;
		final int fromButtonId = fromInterfaceBitmap & 0xFf;
		final int toInterfaceId = toInterfaceBitmap >> 16;
		final int toButtonId = toInterfaceBitmap & 0XFF;

		if ((fromInterfaceId == 747 || toInterfaceId == 662) && toInterfaceBitmap == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(fromSlotId);
				}
			}
			return;
		}
		Item usedWith = player.getInventory().getItem(toSlotId);
		Item itemUsed = player.getInventory().getItem(fromSlotId);
		
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "ItemOnItem " + fromInterfaceId + ", " + fromButtonId + ", " + fromSlotId
					+ ", " + fromItemId + ", " + toInterfaceId + ", " + toButtonId + ", " + toSlotId + ", " + toItemId);
		
		if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromInterfaceId == toInterfaceBitmap
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (fromSlotId >= 28 || toSlotId >= 28 || fromSlotId == toSlotId)
				return;
			
			if (itemUsed == null || usedWith == null || itemUsed.getId() != fromItemId || usedWith.getId() != toItemId)
				return;
			if (player.getMovement().isLocked() || player.getNextEmoteEnd() >= Utility.currentTimeMillis())
				return;
			player.getMovement().stopAll(); 
			if (player.getMapZoneManager().execute(
					controller -> !controller.canUseItemOnItem(player, itemUsed, usedWith))) {
				return;
			}
		}
		
		PassiveSpellDispatcher.executeSpellOnItem(player, fromButtonId, new Item(toItemId), toItemId);
		
		if (Enchanting.cast(player, new Item(toItemId), fromButtonId, toItemId)) {
			return;
		}
		if (Firemaking.execute(player, new Item(toItemId), new Item(fromItemId), false, false, player))
			return;
		if (ItemCombine.handle(player, player.getInventory(), new Item(toItemId), new Item(fromItemId))) {
			return;
		}
		new UseWith(new Item(1511), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogue(d -> {
				BowCarving fletching = new BowCarving(player, Log.NORMAL, false);
				player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
				player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId(),
						fletching.definition.producibles[3].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		new UseWith(new Item(1521), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogue(d -> {
				BowCarving fletching = new BowCarving(player, Log.OAK, false);
				player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
				player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId(),
						fletching.definition.producibles[3].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		new UseWith(new Item(1519), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogue(d -> {
				BowCarving fletching = new BowCarving(player, Log.WILLOW, false);
				player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
				player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId(),
						fletching.definition.producibles[3].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		new UseWith(new Item(1517), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogue(d -> {
				BowCarving fletching = new BowCarving(player, Log.MAPLE, false);
				player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
				player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId(),
						fletching.definition.producibles[3].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		new UseWith(new Item(1515), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogue(d -> {
				BowCarving fletching = new BowCarving(player, Log.YEW, false);
				player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
				player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId(),
						fletching.definition.producibles[3].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		
		new UseWith(new Item(1513), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogue(d -> {
				BowCarving fletching = new BowCarving(player, Log.MAGIC, false);
				player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
				player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
				d.skillsMenu(fletching.definition.producibles[0].producible.getId(),
						fletching.definition.producibles[1].producible.getId(),
						fletching.definition.producibles[2].producible.getId());
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						BowCarving.fletch(player, SkillsDialogue.getItemSlot(button));
					}
				});
			});
		});
		IntStream.of(227, 1761, 1921, 1929, 3735, 19994, 1937, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 7690)
				.filter(id -> fromItemId == id || toItemId == id)
				.forEach(waterSource -> new UseWith(new Item(waterSource), new Item(ItemNames.CLAY_434)).execute(usedWith, itemUsed,
						() -> new SoftClayCreation(player, Filler.values()).start()));
		 
		IntStream.of(2307, 11332, 5980, 2102, 2108, 2114, 11328, 11330, 1963, 7572, 3692, 5982)
				.filter(id -> fromItemId == id || toItemId == id)
				.forEach(slices -> new UseWith(new Item(slices), new Item(946)).execute(usedWith, itemUsed, () -> {
					player.dialogue(d -> {
						d.skillsMenu(FoodSlicing.getDefinition(usedWith.getId(), itemUsed.getId()).get().produced);
						d.skillDialogue(new SkillDialogueFeedback() {
							@Override
							public void handle(int button) {
								FoodSlicing.create(player, usedWith, itemUsed);
							}
						});
					});
				}));

		IntStream.of(227, 1761, 1921, 1929, 3735, 19994, 1937, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 7690)
		.filter(id -> fromItemId == id || toItemId == id)
		.forEach(waterSource -> new UseWith(new Item(waterSource), new Item(1933)).execute(usedWith, itemUsed,
				() -> {
					player.dialogue(d -> {
						d.skillsMenu(DoughCreation.getDefinition(usedWith.getId(), itemUsed.getId()).get().produced);
							d.skillDialogue(new SkillDialogueFeedback() {
								@Override
								public void handle(int button) {
									DoughCreation.create(player, usedWith, itemUsed, waterSource);
								}
							});
					});
				}));
		 
		InventoryPluginDispatcher.execute(player, new Item(fromItemId), new Item(toItemId), toSlotId, fromSlotId);
	}
}