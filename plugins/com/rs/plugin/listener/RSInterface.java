package com.rs.plugin.listener;

import com.rs.game.player.Player;

/**
 * An RS Interface we're going to interact with
 * @author Dennis
 *
 */
public interface RSInterface {
	
	/**
	 * Executes the interface interaction events.
	 * @param player
	 * @param interfaceId
	 * @param componentId
	 * @param packetId
	 * @param slotId
	 * @param slotId2
	 * @throws Exception
	 */
	void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception;
}
