package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

import skills.magic.spells.PassiveSpellDispatcher;

@RSInterfaceSignature(interfaceId = { 430 })
public class LunarSpellbookInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		System.out.println(componentId);
		PassiveSpellDispatcher.execute(player, componentId);
		if (componentId == 5)
			player.getCombatDefinitions().switchShowCombatSpells();
		else if (componentId == 7)
			player.getCombatDefinitions().switchShowTeleportSkillSpells();
		else if (componentId == 9)
			player.getCombatDefinitions().switchShowMiscallaneousSpells();
		else if (componentId >= 11 & componentId <= 13)
			player.getCombatDefinitions().setSortSpellBook(componentId - 11);
		else if (componentId == 20)
			player.getCombatDefinitions().switchDefensiveCasting();
		else
			Magic.processLunarSpell(player, componentId, packetId);
	}
}