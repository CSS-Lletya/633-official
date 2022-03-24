package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 3, description = "Represents a Player's Mouse movement")
public class MouseMovementPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		
	}
}