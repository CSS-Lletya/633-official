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

@LogicPacketSignature(packetId = 37, packetSize = 7, description = "Fourth click packet")
public class ObjectFourthClickPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream input) {
		int x = input.readShortLE();
		int id = input.readShort128();
		boolean forceRun = input.readUnsignedByte128() == 1;
		int y = input.readShort128();
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
		player.setRouteEvent(new RouteEvent(worldObject, () -> {
			if (player.getMapZoneManager().execute(player, controller -> !controller.processObjectClick4(player, worldObject)))
				return;
			ObjectPluginDispatcher.execute(player, worldObject, 4);
		}, true));
	}
}