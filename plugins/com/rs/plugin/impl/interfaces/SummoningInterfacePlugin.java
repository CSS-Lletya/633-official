package com.rs.plugin.impl.interfaces;

import java.util.Optional;

import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 662 })
public class SummoningInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		player.getInterfaceManager().closeInterfaces();
		if (player.getFamiliar() == null) {
			if (player.getPet() == null) {
				return;
			}
			if (componentId == 49)
				player.getPet().call();
			else if (componentId == 51) {
				player.dialogue(d -> {
					d.option(
							(player.getPet() != null ? "Pickup Pet" : "Dismiss Familiar"), () -> {
								if (player.getPet() != null)
									player.getPet().pickup();
								else
									player.getFamiliar().sendDeath(Optional.of(player));
							},
							"Nevermind", () -> player.getInterfaceManager().closeChatBoxInterface()
							);
				});
			return;
			}
		}
		if (componentId == 49)
			player.getFamiliar().call();
		else if (componentId == 51) {
			player.dialogue(d -> {
				d.option(
						(player.getPet() != null ? "Pickup Pet" : "Dismiss Familiar"), () -> {
							if (player.getPet() != null)
								player.getPet().pickup();
							else
								player.getFamiliar().sendDeath(Optional.of(player));
						},
						"Nevermind", () -> player.getInterfaceManager().closeChatBoxInterface()
						);
			});
		} else if (componentId == 67)
			player.getFamiliar().takeBob();
		else if (componentId == 69)
			player.getFamiliar().renewFamiliar();
		else if (componentId == 74) {
			if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
				player.getFamiliar().setSpecial(true);
			if (player.getFamiliar().hasSpecialOn())
				player.getFamiliar().submitSpecial(player);
		}
	}
}