package skills.herblore;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Represents the procession for creating unfinished potions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class UnfinishedPotion extends ProducingSkillAction {
	
	/**
	 * The {@link UnfinishedPotionData} holding all the data required for processing
	 * the creation of {@link UnfinishedPotion}'s.
	 */
	private final UnfinishedPotionData definition;
	
	/**
	 * Constructs a new {@link UnfinishedPotion}.
	 * @param player {@link #getPlayer()}.
	 * @param firstItem the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 */
	public UnfinishedPotion(Player player, Item firstItem, Item secondItem) {
		super(player, Optional.of(player));
		
		definition = UnfinishedPotionData.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
	}
	
	/**
	 * Represents the vial of water item identification.
	 */
	private static final Item VIAL_OF_WATER = new Item(227);
	
	/**
	 * Represents the unfinished potion animation identification.
	 */
	private static final Animation ANIMATION = new Animation(363);
	
	/**
	 * Produces unfinished potions if the player has the requirements required.
	 * @param player {@link #getPlayer()};
	 * @param firstItem the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 * @return <true> if the produce was successful, <false> otherwise.
	 */
	public static boolean produce(Player player, Item firstItem, Item secondItem) {
		UnfinishedPotion potion = new UnfinishedPotion(player, firstItem, secondItem);
		
		if(potion.definition == null) {
			return false;
		}
		potion.start();
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.getPackets().sendGameMessage("You put the " + definition.getHerb().getName() + " Leaf into the vial of water.");
			getPlayer().setNextAnimation(ANIMATION);
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.getHerb(), VIAL_OF_WATER});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.getUnfinishedPotion()});
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
		if(!canProduce()) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExecute() {
		if(!canProduce()) {
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		//No experience is given when producing unfinished potions.
		return 0;
	}
	
	@Override
	public int getSkillId() {
		return Skills.HERBLORE;
	}
	
	public boolean canProduce() {
		if(player.getSkills().getLevel(Skills.HERBLORE) < definition.level) {
			getPlayer().getPackets().sendGameMessage("You need a herblore level of " + definition.getLevel() + " to register " + TextUtils.appendIndefiniteArticle(definition.toString()) + " potion.");
			return false;
		}
		return true;
	}
	
	/**
	 * The data required for processing the creation of unfinished potions.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum UnfinishedPotionData {
		GUAM(249, 91, 1),
		TARROMIN(253, 95, 5),
		MARRENTIL(251, 93, 9),
		HARRALANDER(255, 97, 18),
		RANARR(257, 99, 25),
		TOADFLAX(2998, 3002, 30),
		SPIRIT_WEED(12172, 12181, 35),
		IRIT(259, 101, 40),
		WERGALI(14854, 14856, 41),
		AVANTOE(261, 103, 48),
		KWUARM(263, 105, 54),
		SNAPDRAGON(3000, 3004, 59),
		CADANTINE(265, 107, 65),
		LANTADYME(2481, 2483, 67),
		DWARFWEED(267, 109, 70),
		TORSTOL(269, 111, 75);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<UnfinishedPotionData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(UnfinishedPotionData.class));
		
		/**
		 * The identification for this herb.
		 */
		private final Item herb;
		
		/**
		 * The identification for this unfinished potion.
		 */
		private final Item unfpot;
		
		/**
		 * The required level for creating this potion.
		 */
		private final int level;
		
		/**
		 * Constructs a new {@link UnfinishedPotionData} enum.
		 * @param herb {@link #herb}.
		 * @param unfpot {@link #unfpot}.
		 * @param level {@link #level}.
		 */
		UnfinishedPotionData(int herb, int unfpot, int level) {
			this.herb = new Item(herb);
			this.unfpot = new Item(unfpot);
			this.level = level;
		}
		
		/**
		 * @return {@link #herb}.
		 */
		public Item getHerb() {
			return herb;
		}
		
		/**
		 * @return {@link #unfpot}.
		 */
		public Item getUnfinishedPotion() {
			return unfpot;
		}
		
		/**
		 * @return {@link #level}.
		 */
		public int getLevel() {
			return level;
		}
		
		@Override
		public final String toString() {
			return name().toLowerCase();
		}
		
		/**
		 * Gets the definition for this unfinished potion.
		 * @return an optional holding the {@link UnfinishedPotionData} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<UnfinishedPotionData> getDefinition(int ingredient, int secondIngredient) {
			return VALUES.stream().filter(potion -> potion.getHerb().getId() == ingredient || potion.getHerb().getId() == secondIngredient).filter(potion -> VIAL_OF_WATER.getId() == ingredient || VIAL_OF_WATER.getId() == secondIngredient).findAny();
		}
	}
}
