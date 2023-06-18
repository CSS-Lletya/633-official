package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 60, description = "WorldList Packet")
public class WorldListPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int updateType = stream.readInt();
		player.getPackets().sendWorldList(updateType == 0);
	}
}