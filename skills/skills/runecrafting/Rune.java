package skills.runecrafting;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

import lombok.Getter;
import skills.Skills;

public enum Rune {
	AIR(556, new RunecraftingMultiplier(11, 2), new RunecraftingMultiplier(22, 3), new RunecraftingMultiplier(33, 4), new RunecraftingMultiplier(44, 5), new RunecraftingMultiplier(55, 6), new RunecraftingMultiplier(66, 7), new RunecraftingMultiplier(77, 8), new RunecraftingMultiplier(88, 9), new RunecraftingMultiplier(99, 10)),
	MIND(558, new RunecraftingMultiplier(14, 2), new RunecraftingMultiplier(28, 3), new RunecraftingMultiplier(42, 4), new RunecraftingMultiplier(56, 5), new RunecraftingMultiplier(70, 6), new RunecraftingMultiplier(84, 7), new RunecraftingMultiplier(98, 8)),
	WATER(555, new RunecraftingMultiplier(19, 2), new RunecraftingMultiplier(38, 3), new RunecraftingMultiplier(57, 4), new RunecraftingMultiplier(76, 5), new RunecraftingMultiplier(95, 6)),
	EARTH(557, new RunecraftingMultiplier(26, 2), new RunecraftingMultiplier(52, 3), new RunecraftingMultiplier(78, 4)),
	FIRE(554, new RunecraftingMultiplier(35, 2), new RunecraftingMultiplier(70, 3)),
	BODY(559, new RunecraftingMultiplier(46, 2), new RunecraftingMultiplier(92, 3)),
	COSMIC(564, new RunecraftingMultiplier(59, 2)),
	CHAOS(562, new RunecraftingMultiplier(74, 2)),
	NATURE(561, new RunecraftingMultiplier(91, 2)),
	LAW(563),
	DEATH(560),
	BLOOD(565),
	SOUL(566);

	/**
	 * The item for this rune.
	 */
	@Getter
	private final Item item;

	/**
	 * The multiplier for this rune.
	 */
	@Getter
	private final Optional<RunecraftingMultiplier[]> multiplier;

	/**
	 * Constructs a new {@link Rune} enumerator.
	 * @param item {@link #item}.
	 * @param multiplier {@link #multiplier}.
	 */
	Rune(int item, RunecraftingMultiplier... multiplier) {
		this.item = new Item(item);
		this.multiplier = Optional.of(multiplier);
	}

	/**
	 * Constructs a new {@link Rune} enumerator.
	 * @param item {@link #item}.
	 */
	Rune(int item) {
		this.item = new Item(item);
		this.multiplier = Optional.empty();
	}

	/**
	 * Gets the best multiplier for this player, if none was found it returns
	 * a standard multiplier.
	 * @param player {@link the player we're getting this multiplier for.
	 * @return the best multiplier, otherwise a standard one.
	 */
	public Optional<RunecraftingMultiplier> getBestMultiplier(Player player) {
		if(!multiplier.isPresent())
			return Optional.empty();
		RunecraftingMultiplier best = new RunecraftingMultiplier(1, 1);
		for(RunecraftingMultiplier multiply : multiplier.get()) {
			if (player.getSkills().getLevel(Skills.RUNECRAFTING) >= multiply.getRequirement()) {
				best = multiply;
			}
		}
		return Optional.of(best);
	}

}
