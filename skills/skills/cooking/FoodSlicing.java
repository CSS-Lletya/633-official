package skills.cooking;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.ProducingSkillAction;
import skills.Skills;
import skills.cooking.DoughCreation.DoughData;

public class FoodSlicing extends ProducingSkillAction {

	/**
	 * The data this skill action is dependent of.
	 */
	private final SliceData data;
	
	private int counter;
	
	private Item used, onto;

	/**
	 * Constructs a new {@link FoodSlicing}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public FoodSlicing(Player player, SliceData data, Item used, Item onto) {
		super(player, Optional.empty());
		this.data = data;
		this.used = used;
		this.onto = onto;
	}

	/**
	 * Attempts to register a pie.
	 * @param player the player to register this for.
	 * @param used the item used.
	 * @param usedOn the item used on.
	 * @return {@code true} if the skill action got started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn) {
		SliceData data = getDefinition(used.getId(), usedOn.getId()).orElse(null);
		FoodSlicing creation = new FoodSlicing(player, data,used,usedOn);
		creation.start();
		
		return true;
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(data.produced.getId()).getName()+"_Created").addStatistic("Food_Prepared");
			counter--;
			if(counter == 0)
				t.cancel(); 
		}
	}
	
	/**
	 * Gets the definition for this pie data.
	 * @param ingredient the ingredient to check for.
	 * @return an optional holding the {@link DoughData} value found,
	 * {@link Optional#empty} otherwise.
	 */
	public static Optional<SliceData> getDefinition(int ingredient, int secondIngredient) {
		return SliceData.VALUES.stream().filter(originals -> originals.original.getId() == ingredient
				|| originals.original.getId() == secondIngredient).findAny();
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.original});
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.produced});
	}

	@Override
	public int delay() {
		return 3;
	}

	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		return counter == 0;
	}

	@Override
	public boolean canExecute() {
		return getDefinition(used.getId(), onto.getId())
	            .map(originals -> player.getInventory().containsAny(originals.original.getId()))
	            .orElse(false);
	}

	@Override
	public double experience() {
		return 0;
	}

	@Override
	public int getSkillId() {
		return Skills.COOKING;
	}

	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * register unbaked pies with.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum SliceData {
		BREAD(new Item(2307), new Item(ItemNames.BREADCRUMBS_7515)),
		LEAPING_SURGEN(new Item(ItemNames.LEAPING_STURGEON_11332), new Item(ItemNames.CAVIAR_11326)),
		Calquat_Fruit(new Item(ItemNames.CALQUAT_FRUIT_5980), new Item(ItemNames.CALQUAT_KEG_5769)),
		Lemon(new Item(ItemNames.LEMON_2102), new Item(ItemNames.LEMON_SLICES_2106)), //this has option to pick
		Orange(new Item(ItemNames.ORANGE_2108), new Item(ItemNames.ORANGE_SLICES_2112)), //this has option to pick
		Pineapple(new Item(ItemNames.PINEAPPLE_2114), new Item(ItemNames.PINEAPPLE_CHUNKS_2116)),
		LEAPING_TROUT(new Item(ItemNames.LEAPING_TROUT_11328), new Item(ItemNames.ROE_11324)),
		LEAPING_Salmon(new Item(ItemNames.LEAPING_SALMON_11330), new Item(ItemNames.ROE_11324)),
		Banana(new Item(ItemNames.BANANA_1963), new Item(ItemNames.SLICED_BANANA_3162)),
		Red_Banana(new Item(ItemNames.RED_BANANA_7572), new Item(ItemNames.SLICED_RED_BANANA_7574)),
		Unstrung_lyre(new Item(ItemNames.BRANCH_3692), new Item(ItemNames.UNSTRUNG_LYRE_3688)),
		WATERMELON(new Item(ItemNames.WATERMELON_5982), new Item(ItemNames.WATERMELON_SLICE_5984, 3)),
		;

		/**
		 * Caches our enum values.
		 */
		public static final ImmutableSet<SliceData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SliceData.class));

		/**
		 * The item produced.
		 */
		public final Item original;
		
		/**
		 * The item produced.
		 */
		public final Item produced;

		/**
		 * Constructs a new {@link DoughData}.
		 * @param ingredient {@link #ingredient}.
		 * @param shell {@link #shell}.
		 * @param produced {@link #produced}.
		 */
		SliceData(Item original, Item produced) {
			this.original = new Item(original);
			this.produced = new Item(produced);
		}
	}
}
