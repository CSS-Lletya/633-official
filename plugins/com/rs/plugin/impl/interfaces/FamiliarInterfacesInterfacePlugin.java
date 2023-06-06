package com.rs.plugin.impl.interfaces;

import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 747, 880, 662 })
public class FamiliarInterfacesInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		switch(interfaceId) {
		case 747://orb
			if (componentId == 7 && packetId == 12) {
				Familiar.selectLeftOption(player);
			}
			break;
		case 880:
				if (componentId >= 7 && componentId <= 19)
					Familiar.setLeftclickOption(player, (componentId - 7) / 2);
				else if (componentId == 21)
					Familiar.confirmLeftOption(player);
				else if (componentId == 25)
					Familiar.setLeftclickOption(player, 7);
			break;
		case 662:
			if (player.getFamiliar() == null) {
				if (player.getPet() == null) {
					return;
				}
				if (componentId == 49)
					player.getPet().call();
				else if (componentId == 51)
					player.dialogue(d -> {
						d.option(
								(player.getPet() != null ? "Pickup Pet" : "Dismiss Familiar"), () -> {
									if (player.getPet() != null)
										player.getPet().pickup();
									else
										player.getFamiliar().dissmissFamiliar(false);
								},
								"Nevermind", () -> player.getInterfaceManager().closeChatBoxInterface()
								);
					});
				return;
			}
			if (componentId == 49)
				player.getFamiliar().call();
			else if (componentId == 51)
				player.dialogue(d -> {
					d.option(
							(player.getPet() != null ? "Pickup Pet" : "Dismiss Familiar"), () -> {
								if (player.getPet() != null)
									player.getPet().pickup();
								else
									player.getFamiliar().dissmissFamiliar(false);
							},
							"Nevermind", () -> player.getInterfaceManager().closeChatBoxInterface()
							);
				});
			else if (componentId == 67)
				player.getFamiliar().takeBob();
			else if (componentId == 69)
				player.getFamiliar().renewFamiliar();
			else if (componentId == 74) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
					player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().hasSpecialOn())
					player.getFamiliar().submitSpecial(player);
			}
			break;
		}
	}

}