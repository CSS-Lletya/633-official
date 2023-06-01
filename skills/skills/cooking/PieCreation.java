package skills.cooking;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.ProducingSkillAction;
import skills.Skills;

public final class PieCreation extends ProducingSkillAction {

	/**
	 * A constant representing the pie shell item.
	 */
	private static final Item PIE_SHELL = new Item(2315);

	/**
	 * The data this skill action is dependent of.
	 */
	private final PieData data;

	/**
	 * Constructs a new {@link PieCreation}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public PieCreation(Player player, PieData data) {
		super(player, Optional.empty());
		this.data = data;
	}

	/**
	 * Attempts to register a pie.
	 * @param player the player to register this for.
	 * @param used the item used.
	 * @param usedOn the item used on.
	 * @return {@code true} if the skill action got started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn) {
		PieData data = PieData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		if (data == null)
			return false;
		PieCreation creation = new PieCreation(player, data);
		creation.start();
		return true;
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			t.cancel();
		}
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.shell, data.ingredient});
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.produced});
	}

	@Override
	public int delay() {
		return 2;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public double experience() {
		return 0;
	}

	@Override
	public int getSkillId() {
		return Skills.COOKING;
	}

	private enum PieData {
		POT_OF_FLOUR(1931, 1946, 1933),
		PIE_SHELL_CREATION(1953, 2313, PIE_SHELL.getId()),

		INCOMPLETE_PIZZA(1982, 2283, 2285),
		UNCOOKED_PIZZA(2285, 1985, 2287),

		REDBERRY_PIE(1951, PIE_SHELL.getId(), 2321),
		MEAT_PIE(2140, PIE_SHELL.getId(), 2319),
		MUD_PIE_PART_ONE(6032, PIE_SHELL.getId(), 7164),
		MUD_PIE_PART_TWO(1929, 7164, 7166),
		MUD_PIE(434, 7166, 7168),
		APPLE_PIE(1955, PIE_SHELL.getId(), 2323),
		GARDEN_PIE_PART_ONE(1982, PIE_SHELL.getId(), 7172),
		GARDEN_PIE_PART_TWO(1957, 7172, 7174),
		GARDEN_PIE(1965, 7174, 7176),
		FISH_PIE_PART_ONE(333, PIE_SHELL.getId(), 7182),
		FISH_PIE_PART_TWO(339, 7182, 7184),
		FISH_PIE(1942, 7184, 7186),
		ADMIRAL_PIE_PART_ONE(329, PIE_SHELL.getId(), 7192),
		ADMIRAL_PIE_PART_TWO(361, 7192, 7194),
		ADMIRAL_PIE_PART(1942, 7194, 7196),
		WILD_PIE_PART_ONE(2136, PIE_SHELL.getId(), 7202),
		WILD_PIE_PART_TWO(2876, 7202, 7204),
		WILD_PIE(3226, 7204, 7206),
		SUMMER_PIE_PART_ONE(5504, PIE_SHELL.getId(), 7212),
		SUMMER_PIE_PART_TWO(5982, 7212, 7214),
		SUMMER_PIE(1955, 7214, 7216);

		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<PieData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PieData.class));

		/**
		 * The ingredients required.
		 */
		private final Item ingredient;

		/**
		 * The shell the pie can be created on.
		 */
		private final Item shell;

		/**
		 * The item produced.
		 */
		private final Item produced;

		/**
		 * Constructs a new {@link PieData}.
		 * @param ingredient {@link #ingredient}.
		 * @param shell {@link #shell}.
		 * @param produced {@link #produced}.
		 */
		PieData(int ingredient, int shell, int produced) {
			this.ingredient = new Item(ingredient);
			this.shell = new Item(shell);
			this.produced = new Item(produced);
		}

		/**
		 * Gets the definition for this pie data.
		 * @param ingredient the ingredient to check for.
		 * @return an optional holding the {@link PieData} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<PieData> getDefinition(int ingredient, int secondIngredient) {
			return VALUES.stream().filter(pie -> pie.ingredient.getId() == ingredient || pie.ingredient.getId() == secondIngredient).filter(pie -> pie.shell.getId() == ingredient || pie.shell.getId() == secondIngredient).findAny();
		}
	}
}
