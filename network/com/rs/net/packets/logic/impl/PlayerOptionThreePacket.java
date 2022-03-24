package com.rs.net.packets.logic.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.controller.ControllerHandler;
import com.rs.game.route.RouteEvent;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacket;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 44, packetSize = 3, description = "The Third menu option for a Player")
public class PlayerOptionThreePacket implements LogicPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		final boolean forceRun = stream.readUnsignedByte() == 1;
		int playerIndex = stream.readUnsignedShort128();
		final Player p2 = World.getPlayers().get(playerIndex);
		if (p2 == null || p2 == player || p2.isDead() || p2.isFinished()
				|| !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		if (player.getMovement().isLocked())
			return;
		if (forceRun)
			player.setRun(forceRun);
		player.getMovement().stopAll();
		player.setRouteEvent(new RouteEvent(p2, () ->  {
			if (!ControllerHandler.execute(player, controller -> controller.canPlayerOption3(p2))) {
				return;
			}
		}));
	}
}