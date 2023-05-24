package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.StringInputAction;

import lombok.val;

//TODO: Convert this packet
@OutgoingPacketSignature(packetId = -1, description = "Represents a Longer string of text used for input handling")
public class EnterLongTextPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isRunning() || player.isDead())
			return;
		val value = stream.readString();
		if (value.equals(""))
			return;
		if (player.getAttributes().get(Attribute.STRING_INPUT_ACTION).get() != null) {
			StringInputAction action = (StringInputAction) player.getAttributes().get(Attribute.STRING_INPUT_ACTION).get();
			action.handle(value);
			player.getAttributes().get(Attribute.STRING_INPUT_ACTION).set(null);
			return;
		}
	}
}