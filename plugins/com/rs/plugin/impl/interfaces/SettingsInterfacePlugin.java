package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterface;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 261 })
public class SettingsInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		if (player.getInterfaceManager().containsInventoryInter())
			return;
		if (componentId == 16) {
			if (player.getInterfaceManager().containsScreenInter()) {
				player.getPackets().sendGameMessage(
						"Please close the interface you have open before setting your graphic options.");
				return;
			}
			player.getMovement().stopAll();
			player.getInterfaceManager().sendInterface(742);
		} else if (componentId == 3)
			player.setRun(!player.isRun());
		else if (componentId == 4) {
			player.getDetails().setAllowChatEffects(!player.getDetails().isAllowChatEffects());
			player.getVarsManager().sendVar(171, player.getDetails().isAllowChatEffects() ? 0 : 1);
		} else if (componentId == 5) // chat setup
			player.getInterfaceManager().sendSettings(982);
		else if (componentId == 8) // house options
			player.getInterfaceManager().sendSettings(398);
		else if (componentId == 6) {
			player.getDetails().setMouseButtons(!player.getDetails().isMouseButtons());
			player.getVarsManager().sendVar(170, player.getDetails().isMouseButtons() ? 0 : 1);
		} else if (componentId == 7) {
			player.getDetails().setAcceptAid(!player.getDetails().isAcceptAid());
			player.getVarsManager().sendVar(427, player.getDetails().isAcceptAid() ? 1 : 0);
		} else if (componentId == 18) // audio options
			player.getInterfaceManager().sendInterface(743);
	}
}