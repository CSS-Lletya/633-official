package skills.woodcutting;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Sounds;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

import lombok.Getter;

public enum BirdNest {
	RED_EGG_BIRD_NEST(5070, 4, 5076),
	GREEN_EGG_NEST(5071, 4, 5077),
	PURPLE_EGG_NEST(5072, 4, 5078),
	SEED_NEST(5073, 22, 5312, 5313, 5314, 5315, 5316, 5283, 5284, 5285, 5286, 5287, 5288, 5289, 5290, 5317),
	RING_NEST(5074, 18, 1635, 1637, 1639, 1641, 1643),
	RAVENS_NEST(11966, 8, 1196),
	EMPTY_NEST(7413, 40);

	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<BirdNest> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BirdNest.class));

	/**
	 * Represents an item which boosts the rarity of the drop.
	 */
	private static final Item STRUNG_RABBIT_FEET = new Item(10132);

	/**
	 * The identification for the nest.
	 */
	@Getter
	private final int nest;

	/**
	 * The rarity for this nest.
	 */
	@Getter
	private final int rarity;

	/**
	 * The reward for this nest.
	 */
	@Getter
	private final Item[] reward;

	/**
	 * Constructs a new {@link BirdNest} enum.
	 * @param nest {@link #nest}.
	 * @param rarity {@link #rarity}.
	 * @param reward {@link #reward}.
	 */
	private BirdNest(int nest, int rarity, int... reward) {
		this.nest = nest;
		this.rarity = rarity;
		this.reward = Item.toList(reward);
	}

	/**
	 * Drops a {@link BirdNest} for the specified {@code player}.
	 * @param player the player we're dropping the bird nest for.
	 * @return <true> if the bird nest was dropped, false otherwise.
	 */
	public static void drop(Player player) {
		if(RandomUtility.inclusive(1000) > 5) {
			return;
		}
		boolean modifier = player.getEquipment().containsAny(STRUNG_RABBIT_FEET.getId());
		BirdNest randomNest = RandomUtility.random(VALUES.asList());

		if(RandomUtility.inclusive(100) <= randomNest.rarity + (modifier ? 10 : 0)) {
			FloorItem.addGroundItem(new Item(randomNest.nest), player, player, true, 60);
			player.getPackets().sendGameMessage("A bird's nest falls out of the tree.");
			player.getDetails().getStatistics().addStatistic("Birds_Nest_Found");
			player.getAudioManager().sendSound(Sounds.BIRDS_NEST);
		}
	}
}