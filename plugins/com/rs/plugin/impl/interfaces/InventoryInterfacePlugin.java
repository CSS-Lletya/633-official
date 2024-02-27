package com.rs.plugin.impl.interfaces;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.cache.io.InputStream;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Sounds;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.other.Pet;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.task.Task;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectList;
import skills.cooking.Foods;
import skills.herblore.Potions.Potion;

@RSInterfaceSignature(interfaceId = { 149 })
public class InventoryInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (componentId == 0) {
			if (slotId > 27 || player.getInterfaceManager().containsInventoryInter())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != slotId2)
				return;
			switch (packetId) {
			case 12:
				player.getInventory().sendExamine(slotId);
				InventoryPluginDispatcher.execute(player, item, slotId, 8);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId, 1);
				 if (player.getTreasureTrailsManager().useItem(item, slotId))
			            return;
				if (Foods.eat(player, item, slotId))
					return;
				Potion pot = Potion.forId(item.getId());
				if (pot == null)
					return;
				if (pot == Potion.JUG_OF_BAD_WINE) {
					item.setId(pot.emptyId);
					player.getInventory().refresh(slotId);
					return;
				}
				pot.drink(player, item.getId(), slotId);
				player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(item.getId()).getName() + "_Consumed").addStatistic("Potions_Consumed");
				break;
			case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
				long time = Utility.currentTimeMillis();
				if (player.getDetails().getDisableEquip().isTrue())
					return;
				if (player.getMovement().getLockDelay() >= time || player.getNextEmoteEnd() >= time)
					return;
				player.getMovement().stopAll(false);
				if (InventoryPluginDispatcher.execute(player, item, slotId, 2))
					return;
				if (item.getDefinitions().containsOption("Wield") || item.getDefinitions().containsOption("Wear")) {
					long passedTime = Utility.currentTimeMillis() - Utility.currentWorldCycle();
					if (player.getSwitchItemCache().isEmpty()) {
						player.getSwitchItemCache().add(slotId);
						player.task(passedTime >= 300 ? 0 : passedTime > 150 ? 1 : 0, fighter -> {
							ObjectList<Byte> slots = fighter.toPlayer().getSwitchItemCache();
							int[] slot = new int[slots.size()];
							for (int i = 0; i < slot.length; i++)
								slot[i] = slots.get(i);
							fighter.toPlayer().getSwitchItemCache().clear();
							RSInterfacePluginDispatcher.sendWear(fighter.toPlayer(), slot);
							fighter.toPlayer().getMovement().stopAll(false, true, false);
						});
					} else if (!player.getSwitchItemCache().contains(slotId)) {
						player.getSwitchItemCache().add(slotId);
					}
				}
				
				break;
			case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId, 3);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId, 4);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON5_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId, 5);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON6_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId, 6);
				Potion pots = Potion.forId(item.getId());
				if (pots == null)
					return;
				if (pots == Potion.JUG_OF_BAD_WINE) {
					pots.drink(player, item.getId(), slotId);
					player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(item.getId()).getName() + "_Consumed").addStatistic("Potions_Consumed");
					return;
				}
				if (pots.emptyId != -1) {
					item.setId(pots.emptyId);
					player.getInventory().refresh(slotId);
				}
				break;
			case WorldPacketsDecoder.ACTION_BUTTON7_PACKET:
				long dropTime = Utility.currentTimeMillis();
				if (player.getMovement().getLockDelay() >= dropTime || player.getNextEmoteEnd() >= dropTime)
					return;
				if (player.getMapZoneManager().execute(controller -> !controller.canDropItem(player, item)))
					return;
				player.getMovement().stopAll(false);

				if (item.getDefinitions().isOverSized()) {
					player.getPackets().sendGameMessage("The item appears to be oversized.");
					player.getInventory().deleteItem(item);
					return;
				}
				if (player.getQuestManager().handleDropItem(player, item))
					return;
				if (player.getPetManager().spawnPet(item.getId(), true))
					return;
				if (ItemConstants.isDestroyable(item)) {
					player.getInterfaceManager().sendChatBoxInterface(94);
					player.getAttributes().get(Attribute.DESTROY_ITEM_ID).set(item.getId());
					player.getPackets().sendIComponentText(94, 2, "Are you sure you want to destroy this item?");
					player.getPackets().sendIComponentText(94, 8,
							ItemDefinitions.getItemDefinitions(item.getId()).getName());
					player.getPackets().sendIComponentText(94, 7,
							"The item is undropable, and if dropped could possibly not be obtained again.");
					player.getPackets().sendItemOnIComponent(94, 9, item.getId(), item.getAmount());
					return;
				}
				InventoryPluginDispatcher.execute(player, item, slotId, 7);
				player.getInventory().deleteItem(slotId, item);
				if (IntStream.of(2412, 2413, 2414).anyMatch(cape -> item.getId() == cape)) {
					player.getPackets().sendGameMessage("The cape disintegrates as it touches the ground");
					return;
				}
				if (WildernessMapZone.isAtWild(player) && ItemConstants.isTradeable(item))
					FloorItem.updateGroundItem(item, new WorldTile(player), player, 1, 0);
				else
					FloorItem.updateGroundItem(item, new WorldTile(player), player, 60, 0);
				player.getAudioManager().sendSound(getDropSound(item.getId()));
				break;
			}
		}
	}
	
	private final int getDropSound(int item) {
		if (item == 995)
			return Sounds.COINS_DROPPING_TO_GROUND;
		if (item == 7509)
			return Sounds.ROCKCAKE_DROP_TO_GROUND;
		return Sounds.ITEM_DROPPING_TO_GROUND;
	}

	public static void handleItemOnItem(final Player player, InputStream stream) {
		int toSlot = stream.readShortLE128();
		int fromSlot = stream.readShortLE();
		int itemUsedWithId = stream.readShortLE128();
		int interfaceId2 = stream.readIntLE() >> 16;
		int interfaceId = stream.readIntV2() >> 16;
		int itemUsedId = stream.readShortLE();

		if (GameConstants.DEBUG)
			System.out
					.println(String.format("fromInter: %s, toInter: %s, fromSlot: %s, toSlot %s, item1: %s, item2: %s",
							interfaceId, interfaceId2, fromSlot, toSlot, itemUsedId, itemUsedWithId));

		// fromInter: 44498944, toInter: 44498944, fromSlot: 11694, toSlot 0, item1:
		// 14484, item2: 8

		if ((interfaceId2 == 747 || interfaceId2 == 662) && interfaceId == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}
		if (interfaceId == Inventory.INVENTORY_INTERFACE && interfaceId == interfaceId2
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId)
				return;
			player.getMovement().stopAll();

			if (GameConstants.DEBUG)
				LogUtility.log(LogType.INFO, "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
		}
	}

	public static void handleItemOnNPC(final Player player, final NPC npc, final Item item) {
		player.setRouteEvent(new RouteEvent(npc, () -> {
			if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
				return;
			}
			if (npc instanceof Pet) {
				player.faceEntity(npc);
				player.getPetManager().eat(item.getId(), (Pet) npc);
				return;
			}
		}, true));
	}
}