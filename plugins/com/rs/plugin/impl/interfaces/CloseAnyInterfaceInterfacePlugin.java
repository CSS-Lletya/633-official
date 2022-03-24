package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterface;
import com.rs.plugin.wrapper.RSInterfaceSignature;

/**
 * Mainly used to close interfaces that can't be closed properly.
 * @author Dennis
 *
 */

@RSInterfaceSignature(interfaceId = {429, 982, 398})
public class CloseAnyInterfaceInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		switch(interfaceId) {
		case 429:
			if (componentId == 18)
				player.getInterfaceManager().sendSettings();
			break;
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