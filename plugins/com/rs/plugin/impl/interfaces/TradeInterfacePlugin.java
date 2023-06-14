package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.IntegerInputAction;

@RSInterfaceSignature(interfaceId = { 334, 335, 336 })
public class TradeInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (interfaceId == 334) {
			if (componentId == 21)
				player.getInterfaceManager().closeInterfaces();
			else if (componentId == 20)
				player.getTrade().accept(false);
		} else if (interfaceId == 335) {
//				if (componentId == 62)
//					player.getTrade().changeHours();
			if (componentId == 16)
				player.getTrade().accept(true);
			else if (componentId == 18)
				player.getInterfaceManager().closeInterfaces();
			else if (componentId == 32) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().removeItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getTrade().removeItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getTrade().removeItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getTrade().removeItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getAttributes().get(Attribute.TRADE_ITEM_X_SLOT).set(slotId);
					player.getAttributes().get(Attribute.TRADE_IS_REMOVE).set(true);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getTrade().sendValue(slotId, false);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getTrade().sendExamine(slotId, false);
			} else if (componentId == 35) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().sendValue(slotId, true);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getTrade().sendExamine(slotId, true);
			}
		} else if (interfaceId == 336) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getTrade().addItem(slotId, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getTrade().addItem(slotId, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					player.getTrade().addItem(slotId, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					player.getTrade().addItem(slotId, Integer.MAX_VALUE);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getAttributes().get(Attribute.TRADE_ITEM_X_SLOT).set(slotId);
					player.getAttributes().get(Attribute.TRADE_IS_REMOVE).set(false);
					player.getPackets().sendInputIntegerScript("Enter Amount:", new IntegerInputAction() {
						@Override
						public void handle(int input) {
							// TODO Auto-generated method stub

						}
					});
//				} else if (packetId == 37) {
//					player.getTrade().lendItem(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
					player.getTrade().sendValue(slotId);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		}
	}
}
