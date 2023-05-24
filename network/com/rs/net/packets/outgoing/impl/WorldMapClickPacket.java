package com.rs.net.packets.outgoing.impl;

import com.rs.constants.InterfaceVars;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 23, description = "Represents an interaction with the World Map")
public class WorldMapClickPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int coordinateHash = stream.readIntV2();
		int x = coordinateHash >> 14;
		int y = coordinateHash & 0x3fff;
		int plane = coordinateHash >> 28;
		Integer hash = (Integer) player.getAttributes().get(Attribute.WORLD_HASH).get();
		if (hash == null || coordinateHash != hash)
			player.getAttributes().get(Attribute.WORLD_HASH).set(coordinateHash);
		else {
			player.getAttributes().get(Attribute.WORLD_HASH).set(null);
			player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
			player.getVarsManager().sendVar(InterfaceVars.WORLD_MAP_MARKER, coordinateHash);
		}
	}
}