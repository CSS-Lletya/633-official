package com.rs.game.npc.combat;

import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;

import lombok.Data;

@Data
public class NPCCombatDefinitions {

	public static final int MELEE = 0;
	public static final int RANGE = 1;
	public static final int MAGE = 2;
	public static final int SPECIAL = 3;
	public static final int SPECIAL2 = 4; // follows no distance
	public static final int PASSIVE = 0;
	public static final int AGRESSIVE = 1;

	private int hitpoints;
	private int attackAnim;
	private int defenceAnim;
	private int deathAnim;
	private int attackDelay;
	private int deathDelay;
	private int respawnDelay;
	private int maxHit;
	private int attackStyle;
	private int attackGfx;
	private int attackProjectile;
	private int agressivenessType;

	public NPCCombatDefinitions(int hitpoints, int attackAnim, int defenceAnim, int deathAnim, int attackDelay,
			int deathDelay, int respawnDelay, int maxHit, int attackStyle, int attackGfx, int attackProjectile,
			int agressivenessType) {
		this.hitpoints = hitpoints;
		this.attackAnim = attackAnim;
		this.defenceAnim = defenceAnim;
		this.deathAnim = deathAnim;
		this.attackDelay = attackDelay;
		this.deathDelay = deathDelay;
		this.respawnDelay = respawnDelay;
		this.maxHit = maxHit;
		this.attackStyle = attackStyle;
		this.attackGfx = attackGfx;
		this.attackProjectile = attackProjectile;
		this.agressivenessType = agressivenessType;
	}
	
	public void handleIngoingHit(NPC npc, final Hit hit) {
		if (npc.getCapDamage() != -1 && hit.getDamage() > npc.getCapDamage())
			hit.setDamage(npc.getCapDamage());
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source.isPlayer()) {
			@SuppressWarnings("unused")
			final Player p2 = (Player) source;
//			if (p2.getPrayer().hasPrayersOn()) {
//				if (p2.getPrayer().usingPrayer(1, 18))
//					npc.sendSoulSplit(hit, p2);
//				if (hit.getDamage() == 0)
//					return;
//				if (!p2.getPrayer().isBoostedLeech()) {
//					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
//						if (p2.getPrayer().usingPrayer(1, 19)) {
//							p2.getPrayer().increaseTurmoilBonus(npc);
//							p2.getPrayer().setBoostedLeech(true);
//							return;
//						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
//							// att
//							if (RandomUtils.inclusive(4) == 0) {
//								if (p2.getPrayer().reachedMax(0)) {
//									p2.getPackets().sendGameMessage(
//											"Your opponent has been weakened so much that your sap curse has no effect.",
//											true);
//								} else {
//									p2.getPrayer().increaseLeechBonus(0);
//									p2.getPackets().sendGameMessage(
//											"Your curse drains Attack from the enemy, boosting your Attack.", true);
//								}
//								p2.setNextAnimation(new Animation(12569));
//								p2.setNextGraphics(new Graphics(2214));
//								p2.getPrayer().setBoostedLeech(true);
//								World.sendProjectile(p2, npc, 2215, 35, 35, 20, 5, 0, 0);
//								World.get().submit(new Task(1) {
//									@Override
//									protected void execute() {
//										npc.setNextGraphics(new Graphics(2216));
//										this.cancel();
//									}
//								});
//								return;
//							}
//						} else {
//							if (p2.getPrayer().usingPrayer(1, 10)) {
//								if (RandomUtils.inclusive(7) == 0) {
//									if (p2.getPrayer().reachedMax(3)) {
//										p2.getPackets().sendGameMessage(
//												"Your opponent has been weakened so much that your leech curse has no effect.",
//												true);
//									} else {
//										p2.getPrayer().increaseLeechBonus(3);
//										p2.getPackets().sendGameMessage(
//												"Your curse drains Attack from the enemy, boosting your Attack.", true);
//									}
//									p2.setNextAnimation(new Animation(12575));
//									p2.getPrayer().setBoostedLeech(true);
//									World.sendProjectile(p2, npc, 2231, 35, 35, 20, 5, 0, 0);
//									World.get().submit(new Task(1) {
//										@Override
//										protected void execute() {
//											npc.setNextGraphics(new Graphics(2232));
//											this.cancel();
//										}
//									});
//									return;
//								}
//							}
//							if (p2.getPrayer().usingPrayer(1, 14)) {
//								if (RandomUtils.inclusive(7) == 0) {
//									if (p2.getPrayer().reachedMax(7)) {
//										p2.getPackets().sendGameMessage(
//												"Your opponent has been weakened so much that your leech curse has no effect.",
//												true);
//									} else {
//										p2.getPrayer().increaseLeechBonus(7);
//										p2.getPackets().sendGameMessage(
//												"Your curse drains Strength from the enemy, boosting your Strength.",
//												true);
//									}
//									p2.setNextAnimation(new Animation(12575));
//									p2.getPrayer().setBoostedLeech(true);
//									World.sendProjectile(p2, npc, 2248, 35, 35, 20, 5, 0, 0);
//									World.get().submit(new Task(1) {
//										@Override
//										protected void execute() {
//											npc.setNextGraphics(new Graphics(2250));
//											this.cancel();
//										}
//									});
//									return;
//								}
//							}
//
//						}
//					}
//					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
//						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
//							if (RandomUtils.inclusive(4) == 0) {
//								if (p2.getPrayer().reachedMax(1)) {
//									p2.getPackets().sendGameMessage(
//											"Your opponent has been weakened so much that your sap curse has no effect.",
//											true);
//								} else {
//									p2.getPrayer().increaseLeechBonus(1);
//									p2.getPackets().sendGameMessage(
//											"Your curse drains Range from the enemy, boosting your Range.", true);
//								}
//								p2.setNextAnimation(new Animation(12569));
//								p2.setNextGraphics(new Graphics(2217));
//								p2.getPrayer().setBoostedLeech(true);
//								World.sendProjectile(p2, npc, 2218, 35, 35, 20, 5, 0, 0);
//								World.get().submit(new Task(1) {
//									@Override
//									protected void execute() {
//										npc.setNextGraphics(new Graphics(2219));
//										this.cancel();
//									}
//								});
//								return;
//							}
//						} else if (p2.getPrayer().usingPrayer(1, 11)) {
//							if (RandomUtils.inclusive(7) == 0) {
//								if (p2.getPrayer().reachedMax(4)) {
//									p2.getPackets().sendGameMessage(
//											"Your opponent has been weakened so much that your leech curse has no effect.",
//											true);
//								} else {
//									p2.getPrayer().increaseLeechBonus(4);
//									p2.getPackets().sendGameMessage(
//											"Your curse drains Range from the enemy, boosting your Range.", true);
//								}
//								p2.setNextAnimation(new Animation(12575));
//								p2.getPrayer().setBoostedLeech(true);
//								World.sendProjectile(p2, npc, 2236, 35, 35, 20, 5, 0, 0);
//								World.get().submit(new Task(1) {
//									@Override
//									protected void execute() {
//										npc.setNextGraphics(new Graphics(2238));
//										this.cancel();
//									}
//								});
//								return;
//							}
//						}
//					}
//					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
//						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
//							if (RandomUtils.inclusive(4) == 0) {
//								if (p2.getPrayer().reachedMax(2)) {
//									p2.getPackets().sendGameMessage(
//											"Your opponent has been weakened so much that your sap curse has no effect.",
//											true);
//								} else {
//									p2.getPrayer().increaseLeechBonus(2);
//									p2.getPackets().sendGameMessage(
//											"Your curse drains Magic from the enemy, boosting your Magic.", true);
//								}
//								p2.setNextAnimation(new Animation(12569));
//								p2.setNextGraphics(new Graphics(2220));
//								p2.getPrayer().setBoostedLeech(true);
//								World.sendProjectile(p2, npc, 2221, 35, 35, 20, 5, 0, 0);
//								World.get().submit(new Task(1) {
//									@Override
//									protected void execute() {
//										npc.setNextGraphics(new Graphics(2222));
//										this.cancel();
//									}
//								});
//								return;
//							}
//						} else if (p2.getPrayer().usingPrayer(1, 12)) {
//							if (RandomUtils.inclusive(7) == 0) {
//								if (p2.getPrayer().reachedMax(5)) {
//									p2.getPackets().sendGameMessage(
//											"Your opponent has been weakened so much that your leech curse has no effect.",
//											true);
//								} else {
//									p2.getPrayer().increaseLeechBonus(5);
//									p2.getPackets().sendGameMessage(
//											"Your curse drains Magic from the enemy, boosting your Magic.", true);
//								}
//								p2.setNextAnimation(new Animation(12575));
//								p2.getPrayer().setBoostedLeech(true);
//								World.sendProjectile(p2, npc, 2240, 35, 35, 20, 5, 0, 0);
//								World.get().submit(new Task(1) {
//									@Override
//									protected void execute() {
//										npc.setNextGraphics(new Graphics(2242));
//										this.cancel();
//									}
//								});
//								return;
//							}
//						}
//					}
//
//					// overall
//
//					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
//						if (RandomUtils.inclusive(10) == 0) {
//							if (p2.getPrayer().reachedMax(6)) {
//								p2.getPackets().sendGameMessage(
//										"Your opponent has been weakened so much that your leech curse has no effect.",
//										true);
//							} else {
//								p2.getPrayer().increaseLeechBonus(6);
//								p2.getPackets().sendGameMessage(
//										"Your curse drains Defence from the enemy, boosting your Defence.", true);
//							}
//							p2.setNextAnimation(new Animation(12575));
//							p2.getPrayer().setBoostedLeech(true);
//							World.sendProjectile(p2, npc, 2244, 35, 35, 20, 5, 0, 0);
//							World.get().submit(new Task(1) {
//								@Override
//								protected void execute() {
//									npc.setNextGraphics(new Graphics(2246));
//									this.cancel();
//								}
//							});
//							return;
//						}
//					}
//				}
//			}
		}
	}
}