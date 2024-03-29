package com.rs.game.npc.combat.impl;

import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.MobCombatListener;
import com.rs.game.npc.combat.MobCombatSignature;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

@MobCombatSignature(mobId = {}, mobName = { "Default" })
public class DefaultCombat extends MobCombatListener {

	@Override
	public int execute(NPC npc, Player target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (attackStyle == NPCCombatDefinitions.MELEE) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target)));
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target);
			delayHit(npc, 2, target,
					attackStyle == NPCCombatDefinitions.RANGE ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
			if (defs.getAttackProjectile() != -1)
				World.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 35, 16, 0);
		}
		if (defs.getAttackGfx() != -1)
			npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		npc.setNextAnimation(new Animation(defs.getAttackAnim()));
		return defs.getAttackDelay();
	}
}