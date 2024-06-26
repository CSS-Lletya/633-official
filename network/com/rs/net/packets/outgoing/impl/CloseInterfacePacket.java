package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 50, description = "Represents an Interface closing conditional event")
public class CloseInterfacePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		player.getMovement().stopAll();
	}
}