package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

@OutgoingPacketSignature(packetId = 62, description = "Represents a Screen state that the Player is in")
public class ScreenModePacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		byte displayMode = (byte) stream.readUnsignedByte();
		player.setScreenWidth((short) stream.readUnsignedShort());
		player.setScreenHeight((short) stream.readUnsignedShort());
		@SuppressWarnings("unused")
		boolean switchScreenMode = stream.readUnsignedByte() == 1;
		if (!player.isStarted() || player.isFinished()
				|| displayMode == player.getDisplayMode()
				|| !player.getInterfaceManager().containsInterface(742))
			return;
		player.setDisplayMode(displayMode);
		player.getInterfaceManager().getOpenedinterfaces().clear();
		player.getInterfaceManager().sendInterfaces();
		player.getInterfaceManager().sendInterface(742);
	}
}