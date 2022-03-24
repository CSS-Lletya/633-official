package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 4, description = "Represents a Player's Region processing status")
public class DoneRegionLoadingPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		/*
		 * if(!player.isClientLoadedMapRegion()) { //load objects and items
		 * here player.setClientHasLoadedMapRegion(); }
		 * //player.refreshSpawnedObjects(); //player.refreshSpawnedItems();
		 */
	}
}