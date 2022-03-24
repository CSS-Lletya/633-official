package com.rs.game.player.spells.passive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PassiveSpellSignature {

	/**
	 * The Magic Spell level requirement the player must have in order to cast the
	 * Spell
	 * 
	 * @return level
	 */
	public int spellLevelRequirement();

	/**
	 * The button used for the Magic Spell clicking
	 * 
	 * @return button
	 */
	public int spellButton();
}