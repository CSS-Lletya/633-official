package com.rs.net.packets.logic.impl;

import java.util.Optional;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacket;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 76, packetSize = 3, description = "The Second menu option for a Player")
public class PlayerOptionTwoPacket implements LogicPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		int playerIndex = stream.readUnsignedShort();
		boolean forceRun = stream.readUnsignedByte() == 1;
		Player p2 = World.getPlayers().get(playerIndex);
		if (p2 == null || p2 == player || p2.isDead() || p2.isFinished()
				|| !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		if (player.getMovement().isLocked())
			return;
		if (player.getMapZoneManager().execute(player, controller -> !controller.canPlayerOption2(player, p2))) {
			return;
		}
		if (forceRun)
			player.setRun(forceRun);
		player.getMovement().stopAll();
		player.getAction().setAction(new PlayerFollow(player, Optional.of(p2)));
	}
}