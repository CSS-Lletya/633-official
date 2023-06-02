package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.IntegerInputAction;

@RSInterfaceSignature(interfaceId = { 206, 207 })
public class PriceCheckerInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (interfaceId == 207) {
			switch (packetId) {
			case 11:
				player.getPriceCheckManager().addItem(slotId, 1);
				break;
			case 29:
				player.getPriceCheckManager().addItem(slotId, 5);
				break;
			case 31:
				player.getPriceCheckManager().addItem(slotId, 10);
				break;
			case 9:
				player.getPriceCheckManager().addItem(slotId, Integer.MAX_VALUE);
				break;
			case 32:
				player.getPackets().sendInputIntegerScript("Enter Amount", new IntegerInputAction() {
					@Override
					public void handle(int input) {
						player.getPriceCheckManager().addItem(slotId, input);
					}
				});
				break;
			case 18:
				player.getInventory().sendExamine(slotId);
				break;
			}
		}
		if (interfaceId == 206) {
			switch (packetId) {
			case 11:
				player.getPriceCheckManager().removeItem(slotId, 1);
				break;
			case 29:
				player.getPriceCheckManager().removeItem(slotId, 5);
				break;
			case 31:
				player.getPriceCheckManager().removeItem(slotId, 10);
				break;
			case 9:
				player.getPriceCheckManager().removeItem(slotId, Integer.MAX_VALUE);
				break;
			case 32:
				player.getPackets().sendInputIntegerScript("Enter Amount", new IntegerInputAction() {
					@Override
					public void handle(int input) {
						player.getPriceCheckManager().removeItem(slotId, input);
					}
				});
				break;
			}
		}
	}
}