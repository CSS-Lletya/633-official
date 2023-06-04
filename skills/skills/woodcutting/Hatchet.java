package skills.woodcutting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;

public enum Hatchet {
	BRONZE(1351, 1, 879, 0.1),
	IRON(1349, 1, 877, 0.2),
	STEEL(1353, 6, 875, 0.3),
	BLACK(1361, 6, 873, 0.4),
	MITHRIL(1355, 21, 871, 0.5),
	ADAMANT(1357, 31, 869, 0.6),
	RUNE(1359, 41, 867, 0.75),
	INFERNO_ADZE(13661, 61, 10251, 0.80),
	DRAGON(6739, 61, 2846, 0.85);

	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<Hatchet> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Hatchet.class));

	/**
	 * The identifier for this hatchet.
	 */
	private final Item item;

	/**
	 * The requirement for this hatchet.
	 */
	private final int requirement;

	/**
	 * The animation for this hatchet.
	 */
	private final Animation animation;

	/**
	 * The speed of this hatchet.
	 */
	private final double speed;

	/**
	 * Constructs a new {@link Hatchet} enumerator.
	 * @param item {@link #item}.
	 * @param requirement {@link #requirement}.
	 * @param animation {@link #animation}.
	 * @param speed {@link #speed}.
	 */
	private Hatchet(int item, int requirement, int animation, double speed) {
		this.item = new Item(item);
		this.requirement = requirement;
		this.animation = new Animation(animation);
		this.speed = speed;
	}

	/**
	 * @return {@link #item}.
	 */
	public Item getHatchet() {
		return item;
	}

	/**
	 * @return {@link #requirement}.
	 */
	public int getRequirement() {
		return requirement;
	}

	/**
	 * @return {@link #animation}.
	 */
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * @return {@link #speed}.
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Gets the definition for this hatchet.
	 * @param player Player using the hatchet.
	 * @return an optional holding the {@link Hatchet} value found,
	 * {@link Optional#empty} otherwise.
	 */
	public static Optional<Hatchet> getDefinition(Player player) {
		return VALUES.stream().filter(def -> player.getEquipment().containsAny(def.item.getId()) || player.getInventory().containsAny(def.item.getId())).findAny();
	}
}
