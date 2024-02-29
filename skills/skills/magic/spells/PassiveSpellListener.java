package skills.magic.spells;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * Represents a Passive Magic Spell
 * 
 * @author Dennis
 *
 */
public class PassiveSpellListener {

	/**
	 * Checks if the Player can execute the Spell. In advance this can be used for
	 * additional conditions like Quest requirement in order to access the Magic
	 * Spell.
	 * 
	 * @param player
	 * @return state
	 */
	public boolean canExecute(Player player) {
		return true;
	}
	
	public boolean canExecute(Player player, Item item) {
		return true;
	}

	/**
	 * Executes the Spell
	 * 
	 * @param player
	 * @param entity
	 */
	public void execute(Player player) {
	}
	
	public void execute(Player player, Item item, int slot) {
	}

	/**
	 * A collection of Runes required to cast the Magic Spell
	 * 
	 * @return runes
	 */
	public Item[] runes() {
		return null;
	}
	
	public static final int MODERN = 0, ANCIENT = 1, LUNAR = 2;
}