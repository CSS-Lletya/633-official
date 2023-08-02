package com.rs.game.npc.combat.impl;

import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.MobCombatListener;
import com.rs.game.npc.combat.MobCombatSignature;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Combat;
import com.rs.game.player.Player;
import com.rs.game.player.type.PoisonType;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;

import skills.prayer.book.Prayer;

@MobCombatSignature(mobId = {50}, mobName = {})
public class DragonCombat extends MobCombatListener {

	@Override
	public int execute(Player target, NPC npc) throws Exception {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = RandomUtility.inclusive(5);
		int size = npc.getSize();

		if (attackStyle == 0) {
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)
				attackStyle = RandomUtility.inclusive(4) + 1;
			else {
				delayHit(npc, 0, target,
						getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
				npc.setNextAnimation(new Animation(defs.getAttackAnim()));
				return defs.getAttackDelay();
			}
		} else if (attackStyle == 1 || attackStyle == 2) {
			int damage = RandomUtility.inclusive(650);
			final Player player = target.isPlayer() ? (Player) target : null;
			if (Combat.hasAntiDragProtection(target) || (player != null
					&& (player.getPrayer().active(Prayer.PROTECT_MAGIC) || player.getPrayer().active(Prayer.DEFLECT_MAGIC))))
				damage = 0;
			if (player != null && player.getDetails().getAntifireDetails().isPresent()) {
				player.getPackets().sendGameMessage("Your potion absorbs most of the dragon's breath!");
				if (damage != 0)
					damage = RandomUtility.inclusive(164);
			}
			else if (player != null)
				player.getPackets().sendGameMessage("You are hit by the dragon's fiery breath!", true);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			World.sendProjectile(npc, target, 393, 34, 16, 30, 35, 16, 0);
			npc.setNextAnimation(new Animation(81));

		} else if (attackStyle == 3) {
			int damage;
			final Player player = target.isPlayer() ? (Player) target : null;
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (player != null)
					player.getPackets().sendGameMessage("Your shield absorbs most of the dragon's poisonous breath!",
							true);
			} else if (player != null
					&& (player.getPrayer().active(Prayer.PROTECT_MAGIC) || player.getPrayer().active(Prayer.DEFLECT_MAGIC))) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (player != null)
					player.getPackets().sendGameMessage("Your prayer absorbs most of the dragon's poisonous breath!",
							true);
			} else {
				damage = RandomUtility.inclusive(650);
				if (player != null)
					player.getPackets().sendGameMessage("You are hit by the dragon's poisonous breath!", true);
			}
			if (RandomUtility.inclusive(2) == 0)
				player.poison(PoisonType.SUPER_MAGIC);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			World.sendProjectile(npc, target, 394, 34, 16, 30, 35, 16, 0);
			npc.setNextAnimation(new Animation(81));
		} else if (attackStyle == 4) {
			int damage;
			final Player player = target.isPlayer() ? (Player) target : null;
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (player != null)
					player.getPackets().sendGameMessage("Your shield absorbs most of the dragon's freezing breath!",
							true);
			} else if (player != null
					&& (player.getPrayer().active(Prayer.PROTECT_MAGIC) || player.getPrayer().active(Prayer.DEFLECT_MAGIC))) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (player != null)
					player.getPackets().sendGameMessage("Your prayer absorbs most of the dragon's freezing breath!",
							true);
			} else if (player != null && player.getDetails().getAntifireDetails().isPresent()) {
				player.getPackets().sendGameMessage("Your potion absorbs most of the dragon's breath!");
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (damage != 0)
					damage = RandomUtility.inclusive(164);
			} else {
				damage = RandomUtility.inclusive(650);
				if (player != null)
					player.getPackets().sendGameMessage("You are hit by the dragon's freezing breath!", true);
			}
			if (RandomUtility.inclusive(2) == 0)
				target.getMovement().addFreezeDelay(15000);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			World.sendProjectile(npc, target, 395, 34, 16, 30, 35, 16, 0);
			npc.setNextAnimation(new Animation(81));
		} else {
			int damage;
			final Player player = target.isPlayer() ? (Player) target : null;
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (player != null)
					player.getPackets().sendGameMessage("Your shield absorbs most of the dragon's shocking breath!",
							true);
			} else if (player != null && player.getDetails().getAntifireDetails().isPresent()) {
					player.getPackets().sendGameMessage("Your potion absorbs most of the dragon's breath!");
					damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
					if (damage != 0)
						damage = RandomUtility.inclusive(164);
			} else if (player != null
					&& (player.getPrayer().active(Prayer.PROTECT_MAGIC) || player.getPrayer().active(Prayer.DEFLECT_MAGIC))) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE, target);
				if (player != null)
					player.getPackets().sendGameMessage("Your prayer absorbs most of the dragon's shocking breath!",
							true);
			} else {
				damage = RandomUtility.inclusive(650);
				if (player != null)
					player.getPackets().sendGameMessage("You are hit by the dragon's shocking breath!", true);
			}
			delayHit(npc, 2, target, getRegularHit(npc, damage));
			World.sendProjectile(npc, target, 396, 34, 16, 30, 35, 16, 0);
			npc.setNextAnimation(new Animation(81));
		}
		return defs.getAttackDelay();
	}
}