package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 55, description = "Represents a Click made by the Player ingame")
public class ClickPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		int mouseHash = stream.readShortLE128();
		int mouseButton = mouseHash >> 15;
		int time = mouseHash - (mouseButton << 15); // time
		int positionHash = stream.readIntV1();
		int y = positionHash >> 16; // y;
		int x = positionHash - (y << 16); // x
		@SuppressWarnings("unused")
		boolean clicked;
		// mass click or stupid autoclicker, lets stop lagg
		if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0
				|| y > player.getScreenHeight()) {
			// player.getSession().getChannel().close();
			clicked = false;
			return;
		}
		clicked = true;
	}
}