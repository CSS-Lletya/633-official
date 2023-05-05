package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.Shop;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.Utility;

@OutgoingPacketSignature(packetId = 10, description = "Represents a handler for Interface Component switching")
public class SwitchInterfaceComponentsPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		int fromInterfaceHash = stream.readInt();
		@SuppressWarnings("unused")
		int idk2 = stream.readUnsignedShortLE128();
		int toSlot = stream.readUnsignedShortLE128();
		int toInterfaceHash = stream.readIntV1();
		int fromSlot = stream.readUnsignedShortLE128();
		@SuppressWarnings("unused")
		int idk = stream.readUnsignedShort();

		int toInterfaceId = toInterfaceHash >> 16;
		int toComponentId = toInterfaceHash - (toInterfaceId << 16);
		int fromInterfaceId = fromInterfaceHash >> 16;
		int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);
		
		// System.out.println(fromInterfaceHash + " IDK:" + idk + " "
		// + toInterfaceHash + " " + idk1 + " " + fromSlot + " "
		// + toSlot);
		
		// System.out.println(toInterfaceId + " " + fromInterfaceId + " "
		// + fromComponentId + " " + toComponentId);

		if (Utility.getInterfaceDefinitionsSize() <= fromInterfaceId
				|| Utility.getInterfaceDefinitionsSize() <= toInterfaceId)
			return;
		if (!player.getInterfaceManager()
				.containsInterface(fromInterfaceId)
				|| !player.getInterfaceManager().containsInterface(
						toInterfaceId))
			return;
		if (fromComponentId != -1
				&& Utility.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
			return;
		if (toComponentId != -1
				&& Utility.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
			return;
		if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
				&& fromComponentId == 0
				&& toInterfaceId == Inventory.INVENTORY_INTERFACE
				&& toComponentId == 0) {
			toSlot -= 28;
			if (toSlot < 0
					|| toSlot >= player.getInventory()
							.getItemsContainerSize()
					|| fromSlot >= player.getInventory()
							.getItemsContainerSize())
				return;
			player.getInventory().switchItem(fromSlot, toSlot);
		} else if (fromInterfaceId == 763 && fromComponentId == 0
				&& toInterfaceId == 763 && toComponentId == 0) {
			if (toSlot >= player.getInventory().getItemsContainerSize()
					|| fromSlot >= player.getInventory()
							.getItemsContainerSize())
				return;
			player.getInventory().switchItem(fromSlot, toSlot);
		} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
			player.getBank().switchItem(fromSlot, toSlot, fromComponentId,
					toComponentId);
//		} else if (fromInterfaceId == 1265
//				&& toInterfaceId == 1266
//				&& player.getAttributes().get(Attribute.IS_BUYING).get() != null) {
//			if (player.getAttributes().get(Attribute.IS_BUYING).getBoolean()) {
//				Shop shop = (Shop) player.getAttributes().get(Attribute.SHOP_INSTANCE).get();
//				if (shop == null)
//					return;
//				// shop.buyItem(player, fromSlot, 1);
//			}//this is 718 code, need to rework to 633.
		} else if (fromInterfaceId == 34 && toInterfaceId == 34)
			player.getNotes().switchNotes(fromSlot, toSlot);
		if (GameConstants.DEBUG)
			System.out.println("Switch item " + fromInterfaceId + ", "
					+ fromSlot + ", " + toSlot);
	}
}