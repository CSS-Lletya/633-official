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

public final class PizzaTopping extends ProducingSkillAction {
	
	/**
	 * The topping data this skill action is dependent of.
	 */
	private final ToppingData data;
	
	/**
	 * Constructs a new {@link PizzaTopping}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public PizzaTopping(Player player, ToppingData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	/**
	 * A constant representing the plain pizza item.
	 */
	private static final Item PLAIN_PIZZA = new Item(2289);
	
	/**
	 * Attempts to add a topping to the plain pizza.
	 * @param player {@link #getPlayer()}.
	 * @param used the item used.
	 * @param usedOn the item used on.
	 * @return {@code true} if the skill action was started, {@code false} otherwise.
	 */
	public static boolean add(Player player, Item used, Item usedOn) {
		ToppingData data = ToppingData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		if (data == null)
			return false;
		PizzaTopping cooking = new PizzaTopping(player, data);
		cooking.start();
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
		return Optional.of(new Item[]{data.ingredient, PLAIN_PIZZA});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.product});
	}
	
	@Override
	public int delay() {
		return 3;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean initialize() {
		if(player.getSkills().getLevel(Skills.COOKING) < data.requirement) {
			player.getPackets().sendGameMessage("You need a cooking level of " + data.requirement + " to add this topping.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return data.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.COOKING;
	}
	
	private enum ToppingData {
		MEAT(2142, 2293, 45, 26),
		CHICKEN(2140, 2293, 45, 26),
		ANCHOVY(319, 2297, 55, 39),
		PINEAPPLE_CHUNKS(2116, 2301, 65, 52),
		PINEAPPLE_RING(2118, 2301, 65, 52);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<ToppingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ToppingData.class));
		
		/**
		 * The ingredient of the topping data.
		 */
		private final Item ingredient;
		
		/**
		 * The item produced.
		 */
		private final Item product;
		
		/**
		 * The requirement level to add the topping.
		 */
		private final int requirement;
		
		/**
		 * The experience gained upon adding the topping.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link ToppingData}.
		 * @param itemId {@link #ingredient}.
		 * @param produced {@link #product}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 */
		ToppingData(int itemId, int produced, int requirement, double experience) {
			this.ingredient = new Item(itemId);
			this.product = new Item(produced);
			this.requirement = requirement;
			this.experience = experience;
		}
		
		/**
		 * Gets the definition for this topping data.
		 * @param ingredient the ingredient to check for.
		 * @return an optional holding the {@link ToppingData} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<ToppingData> getDefinition(int ingredient, int secondIngredient) {
			return VALUES.stream().filter(topping -> topping.ingredient.getId() == ingredient || topping.ingredient.getId() == secondIngredient).filter(pie -> PLAIN_PIZZA.getId() == ingredient || PLAIN_PIZZA.getId() == secondIngredient).findAny();
		}
	}
}
