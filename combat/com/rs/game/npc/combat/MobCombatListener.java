package com.rs.game.npc.combat;

import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Steeltitan;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtils;

import skills.Skills;

public abstract class MobCombatListener {
	
	public int execute(Player target, NPC mob) throws Exception {
		return 0;
	}
	
	public static int getRandomMaxHit(NPC npc, int maxHit, int attackStyle, Entity target) {
		short[] bonuses = npc.getBonuses();
		double attack = bonuses == null ? 0
				: attackStyle == NPCCombatDefinitions.RANGE ? bonuses[CombatDefinitions.RANGE_ATTACK]
						: attackStyle == NPCCombatDefinitions.MAGE ? bonuses[CombatDefinitions.MAGIC_ATTACK]
								: bonuses[CombatDefinitions.STAB_ATTACK];
		double defence;
		if (target.isPlayer()) {
			Player targetPlayer = (Player) target;
			defence = targetPlayer.getSkills().getLevel(Skills.DEFENCE)
					+ (2 * targetPlayer.getCombatDefinitions().getBonuses()[attackStyle == NPCCombatDefinitions.RANGE
							? CombatDefinitions.RANGE_DEF
							: attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
									: CombatDefinitions.STAB_DEF]);
			defence *= targetPlayer.getPrayer().getDefenceMultiplier();
			if (attackStyle == NPCCombatDefinitions.MELEE) {
				if (targetPlayer.getFamiliar() instanceof Steeltitan)
					defence *= 1.15;
			}
		} else {
			NPC mobTarget = (NPC) target;
			defence = mobTarget.getBonuses() == null ? 0
					: mobTarget.getBonuses()[attackStyle == NPCCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF
							: attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
									: CombatDefinitions.STAB_DEF];
			defence *= 2;
		}
		double probability = attack / defence;
		if (probability > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
			probability = 0.90;
		else if (probability < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
			probability = 0.05;
		if (probability < Math.random())
			return 0;
		return RandomUtils.inclusive(maxHit);
	}
	
	public static Hit getRangeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.RANGE_DAMAGE);
	}

	public static Hit getMagicHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MAGIC_DAMAGE);
	}

	public static Hit getRegularHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.REGULAR_DAMAGE);
	}

	public static Hit getMeleeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MELEE_DAMAGE);
	}

	public static void delayHit(NPC npc, int delay, final Entity target, final Hit... hits) {
		npc.getCombat().addAttackedByDelay(target);
		World.get().submit(new Task(delay) {
			@Override
			protected void execute() {
				for (Hit hit : hits) {
					NPC npc = (NPC) hit.getSource();
					if (npc.isDead() || npc.isFinished() || target.isDead() || target.isFinished())
						return;
					target.applyHit(hit);
					npc.getCombat().doDefenceEmote(target);
					if (target.isPlayer()) {
						Player targetPlayer = (Player) target;
						targetPlayer.getInterfaceManager().closeInterfaces();
						if (targetPlayer.getCombatDefinitions().isAutoRetaliation() && !targetPlayer.getAction().getAction().isPresent() && !targetPlayer.hasWalkSteps())
							targetPlayer.getAction().setAction(new PlayerCombat(Optional.of(npc)));
						
					} else {
						NPC targetNPC = (NPC) target;
						if (!targetNPC.getCombat().underCombat() || targetNPC.canBeAttackedByAutoRelatie())
							targetNPC.setTarget(npc);
					}

				}
				this.cancel();
			}
		});
	}
}