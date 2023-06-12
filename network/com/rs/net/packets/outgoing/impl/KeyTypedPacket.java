package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 63, description = "Represents a Player's Key input (can be used for ESC close interface, etc..)")
public class KeyTypedPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int key = stream.readByte();
//		if (GameConstants.DEBUG)
//			LogUtility.log(LogType.DEBUG, "Key: " + key);
		if (key == 13) {
			player.getMovement().stopAll(false, true, false);
		}
	}
}