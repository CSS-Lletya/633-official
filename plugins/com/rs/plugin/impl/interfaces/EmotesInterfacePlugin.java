package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.Emotes.Emote;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {464})
public class EmotesInterfacePlugin implements RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		Emote.executeEmote(player, componentId);
	}
}
