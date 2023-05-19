package com.rs.net.packets.logic.impl;

import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteFinder;
import com.rs.game.movement.route.strategy.FixedTileStrategy;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacket;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 43, packetSize = 18, description = "Basic Short-Walking packet")
public class MiniWalkingPacket implements LogicPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		if (player.getMovement().isLocked())
			return;
		if (player.getMovement().getFreezeDelay() >= Utility.currentTimeMillis()) {
			player.getPackets().sendGameMessage("A magical force prevents you from moving.");
			return;
		}
		boolean forceRun = stream.readUnsignedByte128() == 1;
		int baseY = stream.readUnsignedShortLE128();
		int baseX = stream.readUnsignedShortLE();

		player.getMovement().stopAll();
		if (forceRun)
			player.setRun(forceRun);

		if (player.getInterfaceManager().containsScreenInter())
			player.getInterfaceManager().closeInterfaces();

		player.getSkillAction().ifPresent(skill -> skill.cancel());

		int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(),
				player.getSize(), new FixedTileStrategy(baseX, baseY), true);
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		int last = -1;
		for (int i = steps - 1; i >= 0; i--) {
			if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true))
				break;
			last = i;
		}

		if (last != -1) {
			WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
			player.getPackets().sendMinimapFlag(
					tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()),
					tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		} else {
			player.getPackets().sendResetMinimapFlag();
		}
	}
}