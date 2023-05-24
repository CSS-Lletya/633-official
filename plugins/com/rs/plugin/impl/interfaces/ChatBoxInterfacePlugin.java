package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {751})
public class ChatBoxInterfacePlugin implements RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		if (componentId == 9)
			player.getInterfaceManager().sendInterface(914);
		//915 is part 2 of the interface set
		//no functionality, just for archival purposes should someone decide to create one.
	}
}