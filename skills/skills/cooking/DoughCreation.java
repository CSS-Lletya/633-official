package skills.cooking;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction.Filler;
import com.rs.game.task.Task;

import skills.ProducingSkillAction;
import skills.Skills;

public class DoughCreation extends ProducingSkillAction {

	/**
	 * The data this skill action is dependent of.
	 */
	private final DoughData data;
	
	private int counter;
	
	private Item used, onto;

	private int waterSource;
	
	/**
	 * Constructs a new {@link DoughCreation}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public DoughCreation(Player player, DoughData data, Item used, Item onto, int waterSource) {
		super(player, Optional.empty());
		this.data = data;
		this.used = used;
		this.onto = onto;
		this.waterSource = waterSource;
	}

	/**
	 * Attempts to register a pie.
	 * @param player the player to register this for.
	 * @param used the item used.
	 * @param usedOn the item used on.
	 * @return {@code true} if the skill action got started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn, int waterSource) {
		DoughData data = getDefinition(used.getId(), usedOn.getId()).orElse(null);
		DoughCreation creation = new DoughCreation(player, data,used,usedOn, waterSource);
		creation.start();
		return true;
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			Filler.VALUES.stream().filter(water -> water.getFilled().getId() == waterSource)
			.forEach(water -> player.getInventory().replaceItems(new Item(water.getFilled()), new Item(water.getEmpty())));
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(data.produced.getId()).getName()+"_Created").addStatistic("Food_Prepared");
			counter--;
			if(counter == 0)
				t.cancel(); 
		}
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(ItemNames.POT_OF_FLOUR_1933)});
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
	            .map(filled -> player.getInventory().containsAny(ItemNames.POT_OF_FLOUR_1933))
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
	
	public static Optional<DoughData> getDefinition(int ingredient, int secondIngredient) {
		return DoughData.VALUES.stream().filter(originals -> ItemNames.POT_OF_FLOUR_1933 == ingredient
				|| ItemNames.POT_OF_FLOUR_1933 == secondIngredient).findAny();
	}

	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * register unbaked pies with.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum DoughData {
		BREAD(new Item(2307)),
		PASTRY(new Item(1953)),
		PIZZA(new Item(2283)),
		PITTA(new Item(1863))
		;

		/**
		 * Caches our enum values.
		 */
		public static final ImmutableSet<DoughData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(DoughData.class));

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
		DoughData(Item produced) {
			this.produced = new Item(produced);
		}
	}
}
