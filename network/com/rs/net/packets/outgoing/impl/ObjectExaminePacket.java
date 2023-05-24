package com.rs.net.packets.outgoing.impl;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 67, description = "Represents an Object's examine message and or event")
public class ObjectExaminePacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int id = stream.readUnsignedShort();
		String defs = ObjectDefinitions.getObjectDefinitions(id).getName();
		//TODO: Find object description, is it packed in cache? hm
		player.getPackets().sendGameMessage("It's a " + defs + " (ID: " + id +")");
	}
}