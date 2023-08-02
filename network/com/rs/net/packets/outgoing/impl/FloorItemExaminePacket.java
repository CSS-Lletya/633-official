package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.loaders.ItemExamines;

/**
 * @author Savions.
 */
@OutgoingPacketSignature(packetId = 48, description = "Represents a floor item examine action")
public class FloorItemExaminePacket implements OutgoingPacketListener {

	@Override public void execute(Player player, InputStream stream) {
		final int id = stream.readShort();
		player.getPackets().sendGameMessage(ItemExamines.getExamine(id));
	}
}
