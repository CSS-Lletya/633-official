package com.rs.game.npc.combat;

import java.util.concurrent.TimeUnit;

import com.rs.GameConstants;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Combat;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Utility;

import lombok.Data;

@Data
public final class NPCCombat {

	private NPC npc;
	private int combatDelay;
	private Entity target;
	private NPCCombatDispatcher dispatcher;

	public NPCCombat(NPC npc) {
		this.npc = npc;
		setDispatcher(new NPCCombatDispatcher());
	}

	/*
	 * returns if under combat
	 */
	public boolean process() {
		if (combatDelay > 0)
			combatDelay--;
		if (target != null) {
			if (!checkAll()) {
				removeTarget();
				return false;
			}
			if (combatDelay <= 0)
				combatDelay = combatAttack();
			return true;
		}
		return false;
	}

	/*
	 * return combatDelay
	 */
	private int combatAttack() {
		Entity target = this.target; // prevents multithread issues
		if (target == null)
			return 0;
		// if hes frooze not gonna attack
		if (npc.getMovement().getFreezeDelay() >= Utility.currentTimeMillis())
			return 0;
		// check if close to target, if not let it just walk and dont attack
		// this gameticket
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (target instanceof Familiar && RandomUtility.inclusive(3) == 0) {
			Familiar familiar = (Familiar) target;
			Player player = familiar.getOwner();
			if (player != null) {
				target = player;
				npc.setTarget(target);
			}

		}
		// MAGE_FOLLOW and RANGE_FOLLOW follow close but can attack far unlike
		// melee
		int maxDistance = attackStyle == NPCCombatDefinitions.MELEE ? 0 : /* npc instanceof HarAkenTentacle ? 16 : */ 7;
		if (!npc.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target))) {
			return 0;
		}
		if (npc.hasWalkSteps())
			maxDistance += npc.isRun() ? 2 : 1;
		int size = npc.getSize();
		int targetSize = target.getSize();
		if (!Utility.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize, maxDistance))
			return 0;
		if (Utility.colides(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize))
			return 0;
		addAttackedByDelay(target);
		return getDispatcher().customDelay(target.toPlayer(), npc);
	}

	protected void doDefenceEmote(Entity target) {
		target.setNextAnimationNoPriority(new Animation(Combat.getDefenceEmote(target)));
	}

	public void addAttackedByDelay(Entity target) {
		target.setAttackedBy(npc);
		target.setAttackedByDelay(Utility.currentTimeMillis() + npc.getCombatDefinitions().getAttackDelay() * 600 + 600); // 8seconds
	}

	public void setTarget(Entity target) {
		this.target = target;
		npc.setNextFaceEntity(target);
		if (!checkAll()) {
			removeTarget();
			return;
		}
	}

	public boolean checkAll() {
		Entity target = this.target; // prevents multithread issues
		if (target == null)
			return false;
		if (npc.isDead() || npc.isFinished() || npc.isForceWalking() || target.isDead() || target.isFinished()
				|| npc.getPlane() != target.getPlane())
			return false;
		if (npc.getMovement().getFreezeDelay() >= Utility.currentTimeMillis())
			return true; // if freeze cant move ofc
		int distanceX = npc.getX() - npc.getRespawnTile().getX();
		int distanceY = npc.getY() - npc.getRespawnTile().getY();
		int size = npc.getSize();
		int maxDistance;
		Player player = (Player) target;
		boolean agressive = player.getDetails().getTolerance().elapsed(GameConstants.TOLERANCE_SECONDS,
				TimeUnit.SECONDS);
		if (agressive) {
			npc.resetCombat();
			npc.resetWalkSteps();
		}
		if (!npc.isNoDistanceCheck() && !npc.isCantFollowUnderCombat()) {
			maxDistance = 32;
			if (!(npc instanceof Familiar)) {
				if (distanceX > size + maxDistance || distanceX < -1 - maxDistance
						|| distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
					// if more than 64 distance from respawn place
					npc.forceWalkRespawnTile();
					return false;
				}
			}
			maxDistance = 16;
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();
			if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
					|| distanceY < -1 - maxDistance)
				return false; // if target distance higher 16
		} else {
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();
		}
		// checks for no multi area :)
		if (npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			if (!familiar.canAttack(target))
				return false;
		} else {
			if (!npc.isForceMultiAttacked()) {
				if (!target.isMultiArea() || !npc.isMultiArea()) {
					if (npc.getAttackedBy() != target && npc.getAttackedByDelay() > Utility.currentTimeMillis())
						return false;
					if (target.getAttackedBy() != npc && target.getAttackedByDelay() > Utility.currentTimeMillis())
						return false;
				}
			}
		}
		if (!npc.isCantFollowUnderCombat()) {
			// if is under
			int targetSize = target.getSize();
			if (distanceX < size && distanceX > -targetSize && distanceY < size && distanceY > -targetSize
					&& !target.hasWalkSteps()) {

				/*
				 * System.out.println(size + maxDistance); System.out.println(-1 - maxDistance);
				 */
				npc.resetWalkSteps();
				if (!npc.addWalkSteps(target.getX() + 1, npc.getY())) {
					npc.resetWalkSteps();
					if (!npc.addWalkSteps(target.getX() - size, npc.getY())) {
						npc.resetWalkSteps();
						if (!npc.addWalkSteps(npc.getX(), target.getY() + 1)) {
							npc.resetWalkSteps();
							if (!npc.addWalkSteps(npc.getX(), target.getY() - size)) {
								return true;
							}
						}
					}
				}
				return true;
			}
			if (npc.getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE && targetSize == 1
					&& size == 1 && Math.abs(npc.getX() - target.getX()) == 1
					&& Math.abs(npc.getY() - target.getY()) == 1 && !target.hasWalkSteps()) {

				if (!npc.addWalkSteps(target.getX(), npc.getY(), 1))
					npc.addWalkSteps(npc.getX(), target.getY(), 1);
				return true;
			}

			int attackStyle = npc.getCombatDefinitions().getAttackStyle();
			maxDistance = npc.isForceFollowClose() ? 0
					: (attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.SPECIAL2) ? 0
							: 7;
			npc.resetWalkSteps();
			// is far from target, moves to it till can attack
			if ((!npc.clipedProjectile(target, maxDistance == 0)) || distanceX > size + maxDistance
					|| distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
				if (!npc.addWalkStepsInteract(target.getX(), target.getY(), 2, size, true) && combatDelay < 3)
					combatDelay = 3;
				return true;
			}
			// if under target, moves

		}
		return true && agressive;
	}

	private boolean forceCheckClipAsRange(Entity target) {
		return target != null;
	}

	public boolean underCombat() {
		return target != null;
	}

	public void removeTarget() {
		this.target = null;
		npc.setNextFaceEntity(null);
	}

	public void reset() {
		combatDelay = 0;
		target = null;
	}
}