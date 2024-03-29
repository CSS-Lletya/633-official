package com.rs.game.npc.combat.impl;

import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.MobCombatListener;
import com.rs.game.npc.combat.MobCombatSignature;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;

@MobCombatSignature(mobId = {8832, 8833,8834}, mobName = {})
public class LivingRockCreatureCombat extends MobCombatListener {

	@Override
	public int execute(NPC npc, Player target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
		    // TODO add projectile
		    npc.setNextAnimation(new Animation(12196));
		    delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
		} else {
		    npc.setNextAnimation(new Animation(defs.getAttackAnim()));
		    delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, 84, NPCCombatDefinitions.MELEE, target)));
		    return defs.getAttackDelay();
		}

		return defs.getAttackDelay();
		
	}
}