package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.game.item.Item;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import skills.firemaking.Firemaking;
import skills.magic.Enchanting;

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
		if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromInterfaceId == toInterfaceBitmap
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (fromSlotId >= 28 || toSlotId >= 28 || fromSlotId == toSlotId)
				return;
			Item usedWith = player.getInventory().getItem(toSlotId);
			Item itemUsed = player.getInventory().getItem(fromSlotId);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != fromItemId
					|| usedWith.getId() != toItemId)
				return;
			if (player.getMovement().isLocked() || player.getNextEmoteEnd() >= Utility.currentTimeMillis())
				return;
			player.getMovement().stopAll();
			if (player.getMapZoneManager().execute(player, controller -> !controller.canUseItemOnItem(player, itemUsed, usedWith))) {
				return;
			}
		}
		InventoryPluginDispatcher.execute(player, new Item(fromItemId), new Item(toItemId), toSlotId, fromSlotId);
		System.out.println(fromButtonId);
		if (Enchanting.cast(player, new Item(toItemId), fromButtonId, toItemId)) {
			return;
		}
		if (Firemaking.execute(player, new Item(toItemId), new Item(toItemId), false, false, player))
			return;
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "ItemOnItem " + fromInterfaceId + ", " + fromButtonId + ", " + fromSlotId + ", " + fromItemId + ", "
					+ toInterfaceId + ", " + toButtonId + ", " + toSlotId + ", " + toItemId);
	}
}