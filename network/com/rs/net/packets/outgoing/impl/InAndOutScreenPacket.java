package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 16, description = "Represents a Player's Play state with the client itself (visible, etc..)")
public class InAndOutScreenPacket implements OutgoingPacketListener {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, InputStream stream) {
		// not using this check because not 100% efficient
		boolean inScreen = stream.readByte() == 1;
	}
}