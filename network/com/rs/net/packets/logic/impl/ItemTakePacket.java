package com.rs.net.packets.logic.impl;

import com.rs.constants.Animations;
import com.rs.game.item.FloorItem;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

@LogicPacketSignature(packetId = 30, packetSize = 7, description = "Takes an Item from the Ground tile")
public class ItemTakePacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		if (player.getMovement().isLocked())
			return;
		int x = stream.readUnsignedShort128();
		final int id = stream.readUnsignedShort();
		int y = stream.readUnsignedShortLE128();
		boolean forceRun = stream.readByte() == 1;
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
		if (item == null)
			return;
		if (player.getMapZoneManager().execute(player, controller -> !controller.canTakeItem(player, item))) {
			return;
		}
		if (forceRun)
			player.setRun(forceRun);
		player.getMovement().stopAll();
		if (!World.isTileFree(item.getTile().getPlane(), x, y, 1)) {
			player.setNextFaceWorldTile(tile);
			if (FloorItem.removeGroundItem(player, item)) {
				player.setNextAnimation(Animations.OPENING_INFRONT_OF_YOU);
				return;
			}
			return;
		}
		player.setRouteEvent(new RouteEvent(item, () -> {
			if (FloorItem.removeGroundItem(player, item)) {
				LogUtility.log(LogType.INFO,
						player.getDisplayName() + " " + player.getSession().getIP() + " - has picked up item [ id: "
								+ item.getId() + ", amount: " + item.getAmount() + " ] originally owned to "
								+ (item.getOwnerName() == null ? "no owner" : item.getOwnerName()) + ".");
				return;
			}
		}));
	}
}