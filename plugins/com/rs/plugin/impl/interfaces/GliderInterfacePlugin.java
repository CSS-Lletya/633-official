package com.rs.plugin.impl.interfaces;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.traveling.Gliders;
import com.rs.game.task.impl.GliderTask;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 138 })
public class GliderInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		final Gliders glider = Gliders.forId(componentId);
		if (glider == null) {
			return;
		}
		World.get().submit(new GliderTask(1, player, glider));
	}
}