package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

/**
 * This is not an officially declare packet. This is an assumed packet id (correct size however)
 * Either 66 or 58 packet id
 * @author Dennis
 *
 */
@LogicPacketSignature(packetId = 66, packetSize = 15, description = "Something related to Jagex's anti-cheat stuff. NOT FINISHED PACKET")
public class AntiCheatPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		
	}
}