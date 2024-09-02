package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.game.player.queue.impl.WalkScript;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 36, packetSize = 5, description = "Basic Walking packet")
public class WalkingPacket implements LogicPacketListener {

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

		player.getScripts().queue(new WalkScript(baseX, baseY, forceRun));
	}
}