package com.rs.game.player.spells.passive;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * Represents a Passive Magic Spell
 * 
 * @author Dennis
 *
 */
public interface PassiveSpell {

	/**
	 * Checks if the Player can execute the Spell. In advance this can be used for
	 * additional conditions like Quest requirement in order to access the Magic
	 * Spell.
	 * 
	 * @param player
	 * @return state
	 */
	public boolean canExecute(Player player);

	/**
	 * Executes the Spell
	 * 
	 * @param player
	 * @param entity
	 */
	public void execute(Player player);

	/**
	 * A collection of Runes required to cast the Magic Spell
	 * 
	 * @return runes
	 */
	public Item[] runes();
}