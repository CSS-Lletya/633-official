package com.rs.net.packets.outgoing.impl;

import com.rs.constants.InterfaceVars;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 23, description = "Represents an interaction with the World Map")
public class WorldMapClickPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		int coordinateHash = stream.readIntV2();
		int x = coordinateHash >> 14;
		int y = coordinateHash & 0x3fff;
		int plane = coordinateHash >> 28;
		Integer hash = (Integer) player.getAttributes().getAttributes().get(
				"worldHash");
		if (hash == null || coordinateHash != hash)
			player.getAttributes().getAttributes().put("worldHash",
					coordinateHash);
		else {
			player.getAttributes().getAttributes().remove("worldHash");
			player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
			player.getVarsManager().sendVar(InterfaceVars.WORLD_MAP_MARKER, coordinateHash);
		}
	}
}