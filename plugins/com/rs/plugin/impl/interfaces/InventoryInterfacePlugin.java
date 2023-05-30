package com.rs.plugin.impl.interfaces;

import java.util.List;

import com.rs.GameConstants;
import com.rs.cache.io.InputStream;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.cores.WorldThread;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.CoordsEvent;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.other.Pet;
import com.rs.game.player.Equipment;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.Foods;
import com.rs.game.player.content.Potions.Potion;
import com.rs.game.task.Task;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.EquipData;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import skills.Skills;

@RSInterfaceSignature(interfaceId = {149})
public class InventoryInterfacePlugin implements RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (componentId == 0) {
			if (slotId > 27 || player.getInterfaceManager().containsInventoryInter())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != slotId2)
				return;
			switch(packetId) {
			case 12:
				player.getInventory().sendExamine(slotId);
				InventoryPluginDispatcher.execute(player, item, slotId, 8);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId,1);
				if (Foods.eat(player, item, slotId))
					return;
				Potion pot = Potion.forId(item.getId());
				if (pot == null)
					return;
				pot.drink(player, item.getId(), slotId);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
				long time = Utility.currentTimeMillis();
				if (player.isDisableEquip())
					return;
				if (player.getMovement().getLockDelay() >= time || player.getNextEmoteEnd() >= time)
					return;
				player.getMovement().stopAll(false);
				InventoryPluginDispatcher.execute(player, item, slotId,2);
				if (item.getDefinitions().containsOption("Wield") || item.getDefinitions().containsOption("Wear")) {
					long passedTime = Utility.currentTimeMillis() - WorldThread.LAST_CYCLE_CTM;
					if (player.getSwitchItemCache().isEmpty()) {
						player.getSwitchItemCache().add(slotId);
						World.get().submit(new Task(passedTime >= 600 ? 0 : passedTime > 330 ? 1 : 0) {

							@Override
							protected void execute() {
								List<Byte> slots = player.getSwitchItemCache();
								int[] slot = new int[slots.size()];
								for (int i = 0; i < slot.length; i++)
									slot[i] = slots.get(i);
								player.getSwitchItemCache().clear();
								RSInterfacePluginDispatcher.sendWear(player, slot);
								player.getMovement().stopAll(false, true, false);
								this.cancel();
							}
						});
					} else if (!player.getSwitchItemCache().contains(slotId)) {
						player.getSwitchItemCache().add(slotId);
					}
				}
				break;
			case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId,3);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
				InventoryPluginDispatcher.execute(player, item,slotId, 4);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON5_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId,5);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON6_PACKET:
				InventoryPluginDispatcher.execute(player, item, slotId,6);
				Potion pots = Potion.forId(item.getId());
				if (pots == null)
					return;
				if (pots.emptyId != -1) {
					item.setId(pots.emptyId);
					player.getInventory().refresh(slotId);
				}
				break;
			case WorldPacketsDecoder.ACTION_BUTTON7_PACKET:
				long dropTime = Utility.currentTimeMillis();
				if (player.getMovement().getLockDelay() >= dropTime || player.getNextEmoteEnd() >= dropTime)
					return;
				if (player.getMapZoneManager().execute(player, controller -> !controller.canDropItem(player, item)))
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
				if (item.getDefinitions().isDestroyItem()) {
					player.getInterfaceManager().sendChatBoxInterface(94);
					player.getAttributes().get(Attribute.DESTROY_ITEM_ID).set(item.getId());
					player.getPackets().sendIComponentText(94, 2, "Are you sure you want to destroy this item?");
					player.getPackets().sendIComponentText(94, 8, ItemDefinitions.getItemDefinitions(item.getId()).getName());
					player.getPackets().sendIComponentText(94, 7, "The item is undropable, and if dropped could possibly not be obtained again.");
					player.getPackets().sendItemOnIComponent(94, 9, item.getId(), item.getAmount());
					return;
				}
				InventoryPluginDispatcher.execute(player, item, slotId, 7);
				player.getInventory().deleteItem(slotId, item);
				if (player.getDetails().getCharges().degradeCompletly(item))
					return;
				if (WildernessMapZone.isAtWild(player) && ItemConstants.isTradeable(item))
		            FloorItem.updateGroundItem(item, new WorldTile(player), player, 1, 0);
		        else
		        	FloorItem.updateGroundItem(item, new WorldTile(player), player, 1, 0);
				player.getPackets().sendSound(2739, 0, 1);
				break;
			}
		}
	}
	
	public static void handleItemOnItem(final Player player, InputStream stream) {
		int toSlot = stream.readShortLE128();
		int fromSlot = stream.readShortLE();
		int itemUsedWithId = stream.readShortLE128();
		int interfaceId2 = stream.readIntLE() >> 16;
		int interfaceId = stream.readIntV2() >> 16;
		int itemUsedId = stream.readShortLE();
		
		if (GameConstants.DEBUG)
			System.out.println(String.format("fromInter: %s, toInter: %s, fromSlot: %s, toSlot %s, item1: %s, item2: %s", interfaceId, interfaceId2, fromSlot, toSlot, itemUsedId, itemUsedWithId));
		
		//fromInter: 44498944, toInter: 44498944, fromSlot: 11694, toSlot 0, item1: 14484, item2: 8
		
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
		if (item == null) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, () -> {
			if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
				return;
			}
			if (npc instanceof Pet) {
				player.setNextFaceEntity(npc);
				player.getPetManager().eat(item.getId(), (Pet) npc);
				return;
			}
		}, npc.getSize()));
	}
	public static void sendWear(Player player, int[] slotIds) {
		if (player.isFinished() || player.isDead())
			return;
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				continue;
			if (sendWear2(player, slotId, item.getId()))
				worn = true;
		}
		player.getInventory().refreshItems(copy);
		if (worn) {
			player.getAppearance().generateAppearenceData();
			player.getPackets().sendSound(2240, 0, 1);
		}
	}
	
	static int finalSlot;
	public static boolean sendWear2(Player player, int slotId, int itemId) {
		if (player.isFinished() || player.isDead())
			return false;
		player.getMovement().stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId)
			return false;
		if (item.getDefinitions().isNoted()
				|| item.getDefinitions().isWearItem(
						player.getAppearance().isMale()) && itemId != 4084) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		int targetSlot = EquipData.getEquipSlot(itemId);
		finalSlot = targetSlot;
		if (itemId == 4084)
			targetSlot = 3;
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		if (!ItemConstants.canWear(item, player))
			return false;
		boolean isTwoHandedWeapon = targetSlot == 3
				&& Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots()
				&& player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage(
					"Not enough free space in your inventory.");
			return false;
		}
		Object2ObjectArrayMap<Integer, Integer> requiriments = item.getDefinitions()
				.getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequiriments)
						player.getPackets()
								.sendGameMessage(
										"You are not high enough level to use this item.");
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage(
							"You need to have a"
									+ (name.startsWith("a") ? "n" : "") + " "
									+ name + " level of " + level + ".");
				}

			}
		}
		if (!hasRequiriments)
			return false;
		if (player.getMapZoneManager().execute(player, controller -> !controller.canEquip(player, finalSlot, itemId))) {
			return false;
		}
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems()
						.add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment()
							.getItem(3))) {
				if (!player.getInventory().getItems()
						.add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId() || !item
						.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory()
						.getItems()
						.set(slotId,
								new Item(player.getEquipment()
										.getItem(targetSlot).getId(), player
										.getEquipment().getItem(targetSlot)
										.getAmount()));
			} else
				player.getInventory()
						.getItems()
						.add(new Item(player.getEquipment().getItem(targetSlot)
								.getId(), player.getEquipment()
								.getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot,
				targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		if (targetSlot == 3)
			player.getCombatDefinitions().decreaseSpecialAttack(0);
//		player.getDetails().getCharges().wear(targetSlot);
		return true;
	}
}