package com.rs.game.npc.combat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MobCombatSignature {

	/**
	 * This Mob id we're interacting with
	 * @return mobId
	 */
	int[] mobId();
	
	/**
	 * This is the Mob Name we're interacting with
	 * @return mobName
	 */
	String[] mobName();
}
