package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 42, description = "Represents a Player selecting an Item from the Grand Exchange Listing")
public class GrandeExchangeItemSelectPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		@SuppressWarnings("unused")
		int itemId = stream.readUnsignedShort();
//		player.getGeManager().chooseItem(itemId);
	}
}