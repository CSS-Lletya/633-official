package com.rs.net.packets.outgoing;

import com.rs.game.player.Player;
import com.rs.io.InputStream;

/**
 * Represents an Outgoing Packet
 * @author Dennis
 *
 */
public interface OutgoingPacketListener {
	
	/**
	 * Executes the Packet
	 * @param player
	 * @param entity
	 * @throws Exception
	 */
	public void execute(Player player, InputStream stream);
}