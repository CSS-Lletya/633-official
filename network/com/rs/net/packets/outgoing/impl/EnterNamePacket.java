package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 20, description = "Represents an Input of a Player's name")
public class EnterNamePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isRunning() || player.isDead())
			return;
		String value = stream.readString();
		if (value.equals(""))
			return;
		if (player.getInterfaceManager().containsInterface(1108))
			player.getFriendsIgnores().setChatPrefix(value);
	}
}