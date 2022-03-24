package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 63, description = "Represents a Player's Key input (can be used for ESC close interface, etc..)")
public class KeyTypedPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		
	}
}