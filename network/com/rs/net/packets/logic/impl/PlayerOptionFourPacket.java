package com.rs.net.packets.logic.impl;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 51, packetSize = 3, description = "The Fourth menu option for a Player")
public class PlayerOptionFourPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int playerIndex = stream.readUnsignedShortLE128();
		boolean forceRun = stream.readUnsignedByte() == 1;
		final Player p2 = World.getPlayers().get(playerIndex);
		if (p2 == null || p2 == player || p2.isDead() || p2.isFinished()
				|| !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		if (player.getMovement().isLocked())
			return;
		if (forceRun)
			player.setRun(forceRun);
		//request assistance
	}
}