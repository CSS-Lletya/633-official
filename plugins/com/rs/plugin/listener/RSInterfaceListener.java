package com.rs.plugin.listener;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * An RS Interface we're going to interact with
 * @author Dennis
 *
 */
public abstract class RSInterfaceListener {
	
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
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		
	}
	
	public void executeEquipment(Player player, Item item, int componentId, int packetId) {
	}
}
