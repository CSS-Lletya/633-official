package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;

/**
 * @author Savions.
 */

@OutgoingPacketSignature(packetId = 66, description = "Represents an interface being used on a floor item")
public class InterfaceOnFloorItemPacket implements OutgoingPacketListener {

	@Override public void execute(Player player, InputStream stream) {
		final int interfaceBitMap = stream.readIntV1();
		final int interfaceId = interfaceBitMap >> 16;
		final int buttonId = interfaceBitMap & 0XFF;
		final int slotId = stream.readUnsignedShort();
		final int xCoord = stream.readUnsignedShortLE();
		final int yCoord = stream.readUnsignedShort128();
		final int floorItemId = stream.readUnsignedShort128();
		final int componentItemId = stream.readUnsignedShortLE();
		final boolean forceRun = stream.readUnsignedByte() == 1;

		System.out.println("Unhandled Inter on floor item packet; component: " + interfaceId + ", " + buttonId + ", " + slotId + ", " + componentItemId +
				", -> item: " + floorItemId + ", coords[" + xCoord + ", " + yCoord + "], forceRun " + forceRun);
	}
}
