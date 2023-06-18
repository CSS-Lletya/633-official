package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 16, packetSize = 1, description = "Represents the client state of focus on the desktop (minimized/not currently select as viewing)")
public class FocusChangedPacket implements LogicPacketListener {

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, InputStream stream) {
		boolean isFocused = stream.readUnsignedByte() == 1;
	}
}