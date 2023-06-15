package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 71, description = "Represents a Player's idle timer to the server")
public class IdlePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int timer = stream.readShort();
		if (timer % 18000 == 0)
			player.getPackets().sendGameMessage("You have been playing for quite a while now, please consider taking a break!");
	}
}