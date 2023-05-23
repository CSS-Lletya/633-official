package com.rs.net.packets.logic.impl;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacket;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.plugin.ObjectPluginDispatcher;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 58, packetSize = 15, description = "An Interface that's used onto a Object (Magic, etc..)")
public class InterfaceOnObjectPacket implements LogicPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		int slot = stream.readShort();
		int itemId = stream.readUnsignedShortLE();
		int objectId = stream.readUnsignedShortLE();
		int y = stream.readShort();
		boolean forceRun = stream.readByteC() == 1;
		int interfaceHash = stream.readInt();
		final int interfaceId = interfaceHash >> 16;
		int x = stream.readShort128();
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		if (player.getMovement().isLocked() || player.getNextEmoteEnd() >= Utility.currentTimeMillis())
			return;
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		
		GameObject mapObject = GameObject.getObjectWithId(tile, objectId);
		if (mapObject == null || mapObject.getId() != objectId)
			return;
		final GameObject object = mapObject;
		final Item item = player.getInventory().getItem(slot);
		if (player.isDead() || Utility.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if (!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		if (item == null || item.getId() != itemId)
			return;
		player.getMovement().stopAll();
		if (forceRun)
			player.setRun(forceRun);
		switch (interfaceId) {
		case Inventory.INVENTORY_INTERFACE:
			ObjectPluginDispatcher.handleItemOnObject(player, object, interfaceId, item);
			break;
		}
	}
}