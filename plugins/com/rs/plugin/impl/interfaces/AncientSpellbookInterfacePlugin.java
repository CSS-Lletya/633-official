package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.plugin.listener.RSInterface;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {193})
public class AncientSpellbookInterfacePlugin implements RSInterface {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2)
			throws Exception {
		if (componentId == 5)
			player.getCombatDefinitions().switchShowCombatSpells();
		else if (componentId == 7)
			player.getCombatDefinitions().switchShowTeleportSkillSpells();
		else if (componentId >= 9 && componentId <= 11)
			player.getCombatDefinitions().setSortSpellBook(componentId - 9);
		else if (componentId == 18)
			player.getCombatDefinitions().switchDefensiveCasting();
		else
			Magic.processAncientSpell(player, componentId, packetId);
	}
}