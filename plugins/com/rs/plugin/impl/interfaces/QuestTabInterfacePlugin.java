package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 190 })
public class QuestTabInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		player.getPackets().sendRunScript(2160, 0);
		if (componentId == 10) {
			player.getVarsManager().setVarBit(4536, player.getDetails().getQuestSort().isTrue() ? 1 : 0);
		}
		if (componentId == 12) {
			player.getVarsManager().setVarBit(7264, player.getDetails().getHideCompletedQuests().isTrue() ? 0 : 1);
		}
		//quests list is running on slotId
	}
}