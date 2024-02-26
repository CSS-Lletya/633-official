package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

import skills.magic.spells.PassiveSpellDispatcher;

@RSInterfaceSignature(interfaceId = { 192 })
public class ModernSpellbookInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		PassiveSpellDispatcher.execute(player, componentId);
		if (componentId == 2)
			player.getCombatDefinitions().switchDefensiveCasting();
		else if (componentId == 7)
			player.getCombatDefinitions().switchShowCombatSpells();
		else if (componentId == 9)
			player.getCombatDefinitions().switchShowTeleportSkillSpells();
		else if (componentId == 11)
			player.getCombatDefinitions().switchShowMiscallaneousSpells();
		else if (componentId == 13)
			player.getCombatDefinitions().switchShowSkillSpells();
		else if (componentId >= 15 & componentId <= 17)
			player.getCombatDefinitions().setSortSpellBook(componentId - 15);
		else
			Magic.processNormalSpell(player, componentId, packetId);
	}
}