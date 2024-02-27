package com.rs.net.packets.logic.impl;

import com.rs.GameConstants;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.plugin.ObjectPluginDispatcher;

import skills.agility.AgilityHandler;

@LogicPacketSignature(packetId = 75, packetSize = 7, description = "First click packet")
public class ObjectFirstClickPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream input) {
		int x = input.readUnsignedShortLE();
		int y = input.readUnsignedShortLE();
		boolean forceRun = input.readUnsignedByte128() == 1;
		int id = input.readUnsignedShortLE();

		if (GameConstants.DEBUG)
			System.out.println("id " + id + " x " + x + " y " + y + " run? " + forceRun);
		final WorldTile tile = new WorldTile(x, y, player.getPlane());

		final int regionId = tile.getRegionId();

		if (!player.getMapRegionsIds().contains(regionId)) {
			player.getPackets().sendGameMessage("map doesnt contains region");
			return;
		}
		GameObject mapObject = GameObject.getObjectWithId(tile, id);
		if (mapObject == null) {
			return;
		}
		if (player.isDead() || player.getMovement().isLocked()) {
			return;
		}
		if (mapObject.getId() != id) {
			return;
		}
		final GameObject worldObject = mapObject;
		player.getMovement().stopAll();
		if (forceRun)
			player.setRun(forceRun);
		if (isSpecialDistancedObject(player, worldObject)) {
			return;
		}
		player.setRouteEvent(new RouteEvent(worldObject, () -> {
			if (player.getMapZoneManager().execute(controller -> !controller.processObjectClick1(player, worldObject)))
				return;
			if (player.getTreasureTrailsManager().useObject(worldObject))
                return;
			AgilityHandler.execute(player, worldObject);
			ObjectPluginDispatcher.execute(player, worldObject, 1);
		}));
	}

	private boolean isSpecialDistancedObject(Player player, GameObject worldObject) {
		int id = worldObject.getId();
		switch(id) {
		case 43581:
			player.setRouteEvent(new RouteEvent(worldObject, () -> {
				if (player.withinDistance(worldObject, 1))
					AgilityHandler.execute(player, worldObject);
			}, true));
			return true;
		case 43529:
			player.setRouteEvent(new RouteEvent(worldObject, () -> {
				if (player.withinDistance(worldObject, 5))
					AgilityHandler.execute(player, worldObject);
			}, true));
			return true;
		}
		return false;
	}
}