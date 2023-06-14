package com.rs.plugin.impl.interfaces;

import com.rs.content.mapzone.MapZone;
import com.rs.content.mapzone.impl.SawmillMapZone;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.IntegerInputAction;

@RSInterfaceSignature(interfaceId = { 901, 902, 903 })
public class SawmillInterfacesInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		MapZone zone = player.getCurrentMapZone().get();
		if (zone instanceof SawmillMapZone) {
			SawmillMapZone zones = (SawmillMapZone) zone;
				if (interfaceId == 901) {
					if (componentId == 18)
						player.getInterfaceManager().closeInterfaces();
					else if (componentId == 25 || componentId == 34) {
						zones.takeJob(player, componentId == 34);
						player.getInterfaceManager().closeInterfaces();
					}
					return;
				}
			if (interfaceId == 902) {
				if (componentId == 54)
					player.getInterfaceManager().closeInterfaces();
				else if (componentId == 36) {
					if (packetId == 11)
						zones.cutPlank(player, 0, 1);
					else if (packetId == 29)
						zones.cutPlank(player, 0, 5);
					else if (packetId == 31)
						zones.cutPlank(player, 0, 10);
					else if (packetId == 9) {
						player.getPackets().sendInputIntegerScript("Enter amount:", new IntegerInputAction() {
							@Override
							public void handle(int input) {
								zones.addPlank(player, 0, input);
							}
						});
					} else if (packetId == 32)
						zones.cutPlank(player, 0, Integer.MAX_VALUE);
				} else if (componentId == 24) {
					if (packetId == 11)
						zones.cutPlank(player, 1, 1);
					else if (packetId == 29)
						zones.cutPlank(player, 1, 5);
					else if (packetId == 31)
						zones.cutPlank(player, 1, 10);
					else if (packetId == 9) {
						player.getPackets().sendInputIntegerScript("Enter amount:", new IntegerInputAction() {
							@Override
							public void handle(int input) {
								zones.addPlank(player, 1, input);
							}
						});
					} else if (packetId == 32)
						zones.cutPlank(player, 1, Integer.MAX_VALUE);
				} else if (componentId == 30) {
					if (packetId == 11)
						zones.cutPlank(player, 2, 1);
					else if (packetId == 29)
						zones.cutPlank(player, 2, 5);
					else if (packetId == 31)
						zones.cutPlank(player, 2, 10);
					else if (packetId == 9) {
						player.getPackets().sendInputIntegerScript("Enter amount:", new IntegerInputAction() {
							@Override
							public void handle(int input) {
								zones.addPlank(player, 2, input);
							}
						});
					} else if (packetId == 32)
						zones.cutPlank(player, 2, Integer.MAX_VALUE);
				} else if (componentId == 19) {
					if (packetId == 11)
						zones.cutPlank(player, 3, 1);
					else if (packetId == 29)
						zones.cutPlank(player, 3, 5);
					else if (packetId == 31)
						zones.cutPlank(player, 3, 10);
					else if (packetId == 9) {
						player.getPackets().sendInputIntegerScript("Enter amount:", new IntegerInputAction() {
							@Override
							public void handle(int input) {
								zones.addPlank(player, 3, input);
							}
						});
					} else if (packetId == 32)
						zones.cutPlank(player, 3, Integer.MAX_VALUE);
				}
				return;
			}
			if (interfaceId == 903) {
				if (componentId == 18)
					player.getInterfaceManager().closeInterfaces();
				else {
					for (int i = 0; i < SawmillMapZone.INVESTIGATE_COMPONENT_IDS.length; i++) {
						if (componentId == SawmillMapZone.INVESTIGATE_COMPONENT_IDS[i] + 1) {
							if (packetId == 11)
								zones.withdrawFromCart(player, i, 1);
							else if (packetId == 29)
								zones.withdrawFromCart(player, i, 5);
							else if (packetId == 31)
								zones.withdrawFromCart(player, i, 10);
							else {
								player.getPackets().sendInputIntegerScript("Enter amount:", new IntegerInputAction() {
									@Override
									public void handle(int input) {
										zones.withdrawFromCart(player, 3, input);
									}
								});
							}
							return;
						}
					}
				}
				return;
			}
		}
	}

}