package skills.magic;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * The enumerated type whose elements represent the different types of staves.
 * @author lare96 <http://github.com/lare96>
 */
public enum MagicStaff {
	AIR(new int[]{1381, 1397, 1405}, new int[]{556}),
	WATER(new int[]{1383, 1395, 1403}, new int[]{555}),
	EARTH(new int[]{1385, 1399, 1407}, new int[]{557}),
	FIRE(new int[]{1387, 1393, 1401}, new int[]{554}),
	MUD(new int[]{6562, 6563}, new int[]{555, 557}),
	LAVA(new int[]{3053, 3054}, new int[]{554, 557});
	
	/**
	 * Caches our enum values.
	 */
	private static final Set<MagicStaff> VALUES = Sets.immutableEnumSet(EnumSet.allOf(MagicStaff.class));
	
	/**
	 * The current identifiers for this staff type.
	 */
	private final int[] ids;
	
	/**
	 * The runes that this staff type can replace.
	 */
	private final int[] runes;
	
	/**
	 * Creates a new {@link MagicStaff}.
	 * @param ids the current identifiers for this staff type.
	 * @param runes the runes that this staff type can replace.
	 */
	MagicStaff(int[] ids, int[] runes) {
		this.ids = ids;
		this.runes = runes;
	}
	
	/**
	 * Suppresses items in {@code required} if any of the items match the runes
	 * that are represented by the staff {@code player} is wielding.
	 * @param player the player to suppress runes for.
	 * @param required the array of runes to suppress.
	 * @return the new array of items with suppressed runes removed.
	 */
	public static Item[] suppressRunes(Player player, Item[] required) {
		Optional<MagicStaff> staff = getStaff(player.getEquipment().getWeaponId());
		if(!staff.isPresent()) {
			return required;
		}
		for(int rune : staff.get().getRunes()) {
			for(int i = 0; i < required.length; i++) {
				if(required[i] == null)
					continue;
				if(required[i].getId() == rune) {
					required[i] = null;
				}
			}
		}
		return required;
	}
	
	private static Optional<MagicStaff> getStaff(int weapon) {
		for(MagicStaff staff : VALUES) {
			for(int item : staff.getIds()) {
				if(item == weapon) {
					return Optional.of(staff);
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the current identifiers for this staff type.
	 * @return the identifiers for this staff.
	 */
	public final int[] getIds() {
		return ids;
	}
	
	/**
	 * Gets the runes that this staff type can replace.
	 * @return the runes this staff replaces.
	 */
	public final int[] getRunes() {
		return runes;
	}
}
