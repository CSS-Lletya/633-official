package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.IntegerInputAction;

@OutgoingPacketSignature(packetId = 81, description = "Represents an Input of Integer values only.")
public class EnterIntegerPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isRunning() || player.isDead())
			return;
		int value = stream.readInt();
		if (value < 0)
			return;
		
		if (player.getAttributes().get(Attribute.INTEGER_INPUT_ACTION).get() != null) {
			IntegerInputAction action = (IntegerInputAction) player.getAttributes().get(Attribute.INTEGER_INPUT_ACTION).get();
			action.handle(value);
			return;
		}
	}
}