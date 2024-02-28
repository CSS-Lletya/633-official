package com.rs.net.packets.logic.impl;

import com.rs.constants.ItemNames;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

import skills.firemaking.Firemaking;

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

		if (forceRun)
			player.setRun(forceRun);
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);

		player.setRouteEvent(new RouteEvent(item, () -> {
			if (!player.getInventory().contains(new Item(ItemNames.TINDERBOX_590))) {
				player.getPackets().sendGameMessage("You don't have any tinderbox.");
				return;
			} else if (Firemaking.execute(player, new Item(590), new Item(item.getId()), false, true, tile))
				return;
		}));
		
	}
}
