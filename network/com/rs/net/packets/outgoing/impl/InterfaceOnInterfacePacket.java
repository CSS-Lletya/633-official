package com.rs.net.packets.outgoing.impl;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.constants.ItemNames;
import com.rs.game.dialogue.impl.BowFletchingDialogue;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction.Filler;
import com.rs.game.player.content.ItemCombine;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import skills.crafting.SoftClayCreation;
import skills.firemaking.Firemaking;
import skills.fletching.BowCarving.Log;
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
		Item usedWith = player.getInventory().getItem(toSlotId);
		Item itemUsed = player.getInventory().getItem(fromSlotId);
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
		new UseWith(new Item(1511), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogueBlank(new BowFletchingDialogue(player, Log.NORMAL));
		});
		new UseWith(new Item(1521), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogueBlank(new BowFletchingDialogue(player, Log.OAK));
		});
		new UseWith(new Item(1519), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogueBlank(new BowFletchingDialogue(player, Log.WILLOW));
		});
		new UseWith(new Item(1517), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogueBlank(new BowFletchingDialogue(player, Log.MAPLE));
		});
		new UseWith(new Item(1515), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogueBlank(new BowFletchingDialogue(player, Log.YEW));
		});
		new UseWith(new Item(1513), new Item(946)).execute(itemUsed, usedWith, () -> {
			player.dialogueBlank(new BowFletchingDialogue(player, Log.MAGIC));
		});
		IntStream.of(227, 1761, 1921, 1929, 3735, 19994, 1937, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 7690)
				.filter(id -> fromItemId == id || toItemId == id)
				.forEach(waterSource -> new UseWith(new Item(waterSource), new Item(ItemNames.CLAY_434)).execute(usedWith, itemUsed,
						() -> new SoftClayCreation(player, Filler.values()).start()));
		 
		InventoryPluginDispatcher.execute(player, new Item(fromItemId), new Item(toItemId), toSlotId, fromSlotId);
		if (Enchanting.cast(player, new Item(toItemId), fromButtonId, toItemId)) {
			return;
		}
		if (Firemaking.execute(player, new Item(toItemId), new Item(fromItemId), false, false, player))
			return;
		if (ItemCombine.handle(player, player.getInventory(), new Item(toItemId), new Item(fromItemId))) {
			return;
		}
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "ItemOnItem " + fromInterfaceId + ", " + fromButtonId + ", " + fromSlotId
					+ ", " + fromItemId + ", " + toInterfaceId + ", " + toButtonId + ", " + toSlotId + ", " + toItemId);
	}
}