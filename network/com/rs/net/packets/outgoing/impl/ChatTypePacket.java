package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 77, description = "Represents a Chat Type")
public class ChatTypePacket implements OutgoingPacketListener {

	@SuppressWarnings("unused")
	private int chatType;
	
	@Override
	public void execute(Player player, InputStream stream) {
		chatType = stream.readUnsignedByte();
	}
}