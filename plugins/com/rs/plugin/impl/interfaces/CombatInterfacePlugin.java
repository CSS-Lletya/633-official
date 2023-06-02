package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 884 })
public class CombatInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (player.isDead())
			return;
		if (componentId == 4) {
			int weaponId = player.getEquipment().getWeaponId();
			if (player.getCombatDefinitions().hasInstantSpecial(weaponId)) {
				player.getCombatDefinitions().performInstantSpecial(player, weaponId);
				return;
			}
			player.task(1, p -> player.getCombatDefinitions().switchUsingSpecialAttack());
		} else if (componentId >= 11 && componentId <= 14)
			player.getCombatDefinitions().setAttackStyle(componentId - 11);
		else if (componentId == 15)
			player.getCombatDefinitions().switchAutoRelatie();
	}
}