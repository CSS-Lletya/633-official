package com.rs.plugin.impl.interfaces;

import com.rs.game.item.Item;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterface;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.loaders.ItemBonuses;

@RSInterfaceSignature(interfaceId = { 387 })
public class EquipmentInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		if (player.getInterfaceManager().containsInventoryInter())
			return;
		player.getMovement().stopAll();

		if (componentId == 42) {
			if (player.getInterfaceManager().containsScreenInter() || player.getMovement().isLocked()) {
				player.getPackets()
						.sendGameMessage("Please finish what you're doing before opening the price checker.");
				return;
			}
			player.getMovement().stopAll();
			player.getPriceCheckManager().openPriceCheck();

		} else if (componentId == 39) {
			// TODO: Fix sending inventory non x2 times.
			RSInterfacePluginDispatcher.openEquipmentBonuses(player, false);
		}
		if (packetId == 11) {
			if (componentId == 17)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_WEAPON);
			if (componentId == 8)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_HAT);
			if (componentId == 11)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_CAPE);
			if (componentId == 14)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_AMULET);
			if (componentId == 38)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_ARROWS);
			if (componentId == 20)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_CHEST);
			if (componentId == 26)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_LEGS);
			if (componentId == 29)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_HANDS);
			if (componentId == 32)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_FEET);
			if (componentId == 35)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_RING);
			if (componentId == 23)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_SHIELD);
		}
//		else if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_AMULET);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_AMULET);
//		
//		 else if (componentId == 17) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
//				int weaponId = player.getEquipment().getWeaponId();
//				if (weaponId == 20171 || weaponId == 20173) { // zaryte
//																// bow
//					player.getCharges()
//							.checkCharges(
//									player.getEquipment().getItem(3)
//											.getName()
//											+ ": has ## shots remaining.",
//									weaponId);
//				} else if (weaponId == 15484)
//					player.getInterfaceManager().gazeOrbOfOculus();
////				else if (weaponId == 14057) // broomstick
////					SorceressGarden.teleportToSocreressGarden(player, true);
//			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_WEAPON);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_WEAPON);
//		} else if (componentId == 20) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_CHEST);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
//				int chestId = player.getEquipment().getChestId();
//				if (ItemConstants.canCheckCharges(chestId))
//					player.getCharges().checkPercentage(
//							player.getEquipment()
//									.getItem(Equipment.SLOT_CHEST)
//									.getName()
//									+ ": has ##% charge remaining.",
//							chestId, false);
//			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_CHEST);
//		} else if (componentId == 23) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_SHIELD);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_SHIELD);
//
//		} else if (componentId == 26) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_LEGS);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
//				int legsId = player.getEquipment().getLegsId();
//				if (ItemConstants.canCheckCharges(legsId))
//					player.getCharges().checkPercentage(
//							player.getEquipment()
//									.getItem(Equipment.SLOT_LEGS).getName()
//									+ ": has ##% charge remaining.",
//							legsId, false);
//			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_LEGS);
//		} else if (componentId == 29) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_HANDS);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_HANDS);
//		} else if (componentId == 32) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_FEET);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_FEET);
//		} else if (componentId == 35) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_RING);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_RING);
//		} else if (componentId == 38) {
//			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
//				RSInterfaceDispatcher.sendRemove(player, Equipment.SLOT_ARROWS);
//			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
//				player.getEquipment().sendExamine(Equipment.SLOT_ARROWS);
//		} else if (componentId == 45) {
//			player.stopAll();
//			RSInterfaceDispatcher.openItemsKeptOnDeath(player);
//		} else if (componentId == 41) {
//			player.stopAll();
//			player.getInterfaceManager().sendInterface(1178);
//		} else if (componentId == 39) {
//			RSInterfaceDispatcher.openEquipmentBonuses(player, false);
//		}
	}

	public static void sendItemStats(final Player player, Item item) {
		StringBuilder b = new StringBuilder();
		if (item.getId() == 772)
			return;
		boolean hasBonuses = ItemBonuses.getItemBonuses(item.getId()) != null;
		for (int i = 0; i < 17; i++) {
			int bonus = hasBonuses ? ItemBonuses.getItemBonuses(item.getId())[i] : 0;
			String label = CombatDefinitions.BONUS_LABELS[i];
			String sign = bonus > 0 ? "+" : "";
			if (bonus == 16) {
				continue;
			}
			b.append(label + ": " + (sign + bonus) + ((label == "Magic Damage" || label == "Absorb Melee"
					|| label == "Absorb Magic" || label == "Absorb Ranged") ? "%" : "") + "<br>");
		}
		player.getPackets().sendGlobalString(321, "Stats for " + item.getName());
		player.getPackets().sendGlobalString(324, b.toString());
		player.getPackets().sendHideIComponent(667, 49, false);
		player.setCloseInterfacesEvent(() -> {
			player.getPackets().sendGlobalString(321, "");
			player.getPackets().sendGlobalString(324, "");
			player.getPackets().sendHideIComponent(667, 49, true);
		});
	}
}