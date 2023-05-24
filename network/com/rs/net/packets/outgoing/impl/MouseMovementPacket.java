package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 3, description = "Represents a Player's Mouse movement")
public class MouseMovementPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		
	}
}