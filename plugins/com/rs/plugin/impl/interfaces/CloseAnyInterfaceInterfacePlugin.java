package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

/**
 * Mainly used to close interfaces that can't be closed properly.
 * 
 * @author Dennis
 *
 */

@RSInterfaceSignature(interfaceId = { 982, 398 })
public class CloseAnyInterfaceInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		switch (interfaceId) {
		case 982:
			if (componentId == 5)
				player.getInterfaceManager().sendSettings();
			break;
		case 398:
			if (componentId == 19)
				player.getInterfaceManager().sendSettings();
			break;
		}
	}
}