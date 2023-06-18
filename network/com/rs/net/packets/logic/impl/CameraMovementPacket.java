package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 5, packetSize = 4, description = "Represents the Camera movement state changing")
public class CameraMovementPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		int a = stream.readShort();
		int b = stream.readShort();
		System.out.println(a + " - " + b);
		
	}
}