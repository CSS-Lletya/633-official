package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 8, description = "Represents a Player's AFK state")
public class AFKPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		//player.getSession().getChannel().close();
	}
}