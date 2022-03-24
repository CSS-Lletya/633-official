package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.StringInputAction;

import lombok.val;

//TODO: Convert this packet
@OutgoingPacketSignature(packetId = -1, description = "Represents a Longer string of text used for input handling")
public class EnterLongTextPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isRunning() || player.isDead())
			return;
		val value = stream.readString();
		if (value.equals(""))
			return;
		if (player.getAttributes().getAttributes().get("string_input_action") != null) {
			StringInputAction action = (StringInputAction) player.getAttributes().getAttributes().remove("string_input_action");
			action.handle(value);
			return;
		}
	}
}