package com.rs.net.packets.logic.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 71, packetSize = 2, description = "Report Abuse Packet, not finished packet.")
public class ReportAbusePacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		stream.readUnsignedShort();
	}
}