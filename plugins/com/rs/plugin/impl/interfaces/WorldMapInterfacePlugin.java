package com.rs.plugin.impl.interfaces;

import java.util.Optional;

import com.rs.constants.InterfaceVars;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Rest;
import com.rs.plugin.listener.RSInterface;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.Utility;

@RSInterfaceSignature(interfaceId = { 750, 548, 755, 746})
public class WorldMapInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		switch (interfaceId) {
		case 750:
			if (componentId == 1 && packetId == 11) {
				if (player.getMovement().isResting())
					player.getMovement().stopAll();
				player.setRunState(!player.isRun());	
			}
			if (componentId == 1 && packetId == 29) {
				if (player.getMovement().isResting()) {
					player.getMovement().stopAll();
					return;
				}
				long currentTime = Utility.currentTimeMillis();
				if (player.getNextEmoteEnd() >= currentTime) {
					player.getPackets().sendGameMessage("You can't rest while perfoming an emote.");
					return;
				}
				if (player.getMovement().getLockDelay() >= currentTime) {
					player.getPackets().sendGameMessage("You can't rest while perfoming an action.");
					return;
				}
				player.getMovement().stopAll();
				player.getAction().setAction(new Rest(player, Optional.empty()));
			}
			break;
		case 548:
			if (componentId == 0 && packetId == 19)
				player.getSkills().resetXpCounter();
			if (componentId == 178 && packetId == 11) {
				// world map open
				player.getPackets().sendRootInterface(755, 0);
				int posHash = player.getX() << 14 | player.getY();
				player.getPackets().sendGlobalConfig(622, posHash); // map open
				// center
				// pos
				player.getPackets().sendGlobalConfig(674, posHash); // player
				// position
			}
			break;
		case 755:
			if (componentId == 44)
				player.getPackets().sendRootInterface(player.getInterfaceManager().isResizableScreen() ? 746 : 548, 2);
			else if (componentId == 42) {
				player.getHintIconsManager().removeAll();// TODO find hintIcon index
				player.getVarsManager().sendVar(InterfaceVars.WORLD_MAP_MARKER, 1);
			}
			break;
		case 746:
			if (componentId == 223 && packetId == 19)
				player.getSkills().resetXpCounter();
			if (componentId == 177 && packetId == 11) {
				// world map open
				player.getPackets().sendRootInterface(755, 0);
				int posHash = player.getX() << 14 | player.getY();
				player.getPackets().sendGlobalConfig(622, posHash); // map open
				// center
				// pos
				player.getPackets().sendGlobalConfig(674, posHash); // player
				// position
			}
			break;
		}
	}
}