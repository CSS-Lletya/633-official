package com.rs.game.player.spells.passive.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;

import skills.Skills;

/**
 * @author Emperor
 * @author Dennis
 *
 */
@PassiveSpellSignature(spellButton = 47, spellLevelRequirement = 77, spellbookId = 2, experience = 78)
public class SuperGlassMakeSpellPlugin implements PassiveSpellListener {
	
	/**
	 * The set of items to be used with sand.
	 */
	private static final Item[] SETS = new Item[] {new Item(1781), new Item(401), new Item(10978)};
	
	/**
	 * Represents the bucket of sand item.
	 */
	private static final Item BUCKET_OF_SAND = new Item(1783);

	/**
	 * Represents the molten glass item.
	 */
	private static final Item MOLTEN_GLASS = new Item(1775);
	
	@Override
	public boolean canExecute(Player player) {
		if (!player.getInventory().containsItem(1783, 1) || !player.getInventory().containsItem(401, 1)) {
			player.getPackets().sendGameMessage("You don't have the required ingredients.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(4);
		player.getDetails().getComponentLockTimer().start(3);
		player.setNextAnimation(Animations.SUPERGLASS_MAKE);
		player.setNextGraphics(Graphic.SUPERGLASS_MAKE);
		player.getAudioManager().sendSound(Sounds.SUPERGLASS_MAKE);
		
		int setIndex = getSetIndex(player);
		int sand = player.getInventory().getAmountOf(BUCKET_OF_SAND.getId());
		if (setIndex == -1) {
			player.getPackets().sendGameMessage("You don't have the required ingredients.");
			return ;
		}
		
		for (int i = 0; i < sand; i++) {
			if (hasSet(player, setIndex)) {
				if (player.getInventory().removeItems(BUCKET_OF_SAND, SETS[setIndex])) {
					player.getInventory().addItem(MOLTEN_GLASS);
					player.getSkills().addExperience(Skills.CRAFTING, 10);
				}
			}
		}	
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2),
				new Item(ItemNames.FIRE_RUNE_554, 6),
				new Item(ItemNames.AIR_RUNE_556, 10),
		};
	}
	
	/**
	 * Checks if the player has the set.
	 * @param player the player.
	 * @param index the index.
	 * @return {@code True} if so.
	 */
	public boolean hasSet(final Player player, final int index) {
		return player.getInventory().containsItem(BUCKET_OF_SAND) && player.getInventory().containsItem(SETS[index]);
	}

	/**
	 * Gets the set index.
	 * @param player the player.
	 * @return the index.s
	 */
	public int getSetIndex(final Player player) {
		if (!player.getInventory().containsItem(BUCKET_OF_SAND)) {
			return -1;
		}
		for (int i = 0; i < SETS.length; i++) {
			if (player.getInventory().containsItem(SETS[i])) {
				return i;
			}
		}
		return -1;
	}
}