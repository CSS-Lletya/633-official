package com.rs.game.npc.combat.impl;

import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.MobCombatListener;
import com.rs.game.npc.combat.MobCombatSignature;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;

@MobCombatSignature(mobId = {}, mobName = {"Default"})
public class DefaultCombat extends MobCombatListener {

	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		return defs.getAttackDelay();
		
	}
}