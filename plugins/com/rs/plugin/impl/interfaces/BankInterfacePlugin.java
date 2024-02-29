package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.IntegerInputAction;

//TODO: Update ids
@RSInterfaceSignature(interfaceId = { 11, 762, 763 })
public class BankInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int option, byte slotId, int slotId2) {
		if (interfaceId == 11) {
			if (componentId == 17) {
				if (option == 11)
					player.getBank().depositItem(slotId, 1, true);
				else if (option == 29)
					player.getBank().depositItem(slotId, 5, true);
				else if (option == 31)
					player.getBank().depositItem(slotId, 10, true);
				else if (option == 9)
					player.getBank().depositItem(slotId, player.getInventory().getAmountOf(slotId2), true);
				else if (option == 32) {
					player.getPackets().sendInputIntegerScript("How many would you like to deposit?",
							new IntegerInputAction() {
								@Override
								public void handle(int input) {
									player.getBank().depositItem(slotId, input, true);
								}
							});
				} else if (option == 18)
					player.getInventory().sendExamine(slotId);
			} else if (componentId == 18)
				player.getBank().depositAllInventory(false);
			else if (componentId == 20) // 22 is bob
				player.getBank().depositAllEquipment(false);
		} else if (interfaceId == 762) {
			if (player.getAttributes().get(Attribute.IS_BANKING).getBoolean()) {
				if (componentId == 117) {
					RSInterfacePluginDispatcher.openEquipmentBonuses(player, true);
				}
				if (componentId == 37 && option == 11)
					player.getBank().depositAllBob(true);
				if (componentId == 15)
					player.getBank().switchInsertItems();
				else if (componentId == 17) {
					player.getPackets().sendGlobalConfig(190, 1);
					player.getPackets().sendGlobalConfig(11, -1);
				} else if (componentId == 19)
					player.getBank().switchWithdrawNotes();
				else if (componentId == 33)
					player.getBank().depositAllInventory(true);
				else if (componentId == 35) {
					player.getBank().depositAllEquipment(true);
				} else if (componentId == 44) {
					player.getInterfaceManager().closeInterfaces();
					player.getInterfaceManager().sendInterface(767);
				} else if (componentId >= 46 && componentId <= 64) {
					int tabId = 8 - ((componentId - 46) / 2);
					if (option == 11)
						player.getBank().setCurrentTab(tabId);
					if (option == 29)
						player.getBank().collapse(tabId);
				} else if (componentId == 93) {
					if (option == 11)
						player.getBank().withdrawItem(slotId, 1);
					else if (option == 29)
						player.getBank().withdrawItem(slotId, 5);
					else if (option == 31)
						player.getBank().withdrawItem(slotId, 10);
					else if (option == 4)
						player.getBank().withdrawLastAmount(slotId);
					else if (option == 5) {
						player.getPackets().sendInputIntegerScript("How many would you like to deposit?",
								new IntegerInputAction() {
									@Override
									public void handle(int input) {
										player.getBank().withdrawItem(slotId, input);
									}
								});
					} else if (option == 18)
						player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
					else if (option == 72)
						player.getBank().withdrawItemButOne(slotId);
					else if (option == 12)
						player.getBank().sendExamine(slotId);

				} else if (componentId == 119) {
					RSInterfacePluginDispatcher.openEquipmentBonuses(player, true);
				}
			}
		} else if (interfaceId == 763) {
			if (componentId == 0) {
				if (player.getAttributes().get(Attribute.IS_BANKING).getBoolean()) {
					if (option == 11)
						player.getBank().depositItem(slotId, 1, true);
					else if (option == 29)
						player.getBank().depositItem(slotId, 5, true);
					else if (option == 31)
						player.getBank().depositItem(slotId, 10, true);
					else if (option == 9)
						player.getBank().depositLastAmount(slotId);
					else if (option == 32) {
						player.getPackets().sendInputIntegerScript("How many would you like to deposit?",
								new IntegerInputAction() {
									@Override
									public void handle(int input) {
										player.getBank().depositItem(slotId, input, true);
									}
								});
					} else if (option == 18)
						player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
					else if (option == 12)
						player.getInventory().sendExamine(slotId);
				}
			}
		}
	}
}