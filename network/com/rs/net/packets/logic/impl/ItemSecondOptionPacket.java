package com.rs.net.packets.logic.impl;

import com.rs.game.item.FloorItem;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

/**
 * @author Savions.
 */
@LogicPacketSignature(packetId = 84, packetSize = 7, description = "Second option for floor items")
public class ItemSecondOptionPacket implements LogicPacketListener {

	@Override public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		if (player.getMovement().isLocked())
			return;
		int y = stream.readUnsignedShort128();
		boolean forceRun = stream.readByte() == 1;
		final int id = stream.readShortLE();
		int x = stream.readShortLE();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();

		if (!player.getMapRegionsIds().contains(regionId))
			return;
		final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);

	}
}
