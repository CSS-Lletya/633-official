package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 3, packetSize = -1, description = "Mouse Movement packet. NOT FINISHED PACKET")
public class MouseMovementPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		//search client for code. 
		// if ((i_64_ ^ 0xffffffff) != (Class120.anInt1628 ^ 0xffffffff)
	}
}