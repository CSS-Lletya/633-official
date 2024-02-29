package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 741 })
public class LevelUpInterface extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (componentId == 10) {
			player.getInterfaceManager().closeInterfaces();
			SkillGuideInterfacePlugin.sendSkillGuide(player, player.getDetails().getLastSkillMenu());
		}
	}
}