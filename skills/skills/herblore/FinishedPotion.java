package skills.herblore;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Represents the procession for creating unfinished potions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FinishedPotion extends ProducingSkillAction {
	
	/**
	 * The {@link FinishedPotion} holding all the data required for processing
	 * the creation of {@link UnfinishedPotion}'s.
	 */
	private final FinishedPotionData definition;
	
	/**
	 * Constructs a new {@link UnfinishedPotion}.
	 * @param player {@link #getPlayer()}.
	 * @param firstItem the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 */
	public FinishedPotion(Player player, Item firstItem, Item secondItem) {
		super(player, Optional.of(player));
		definition = FinishedPotionData.getDefinition(firstItem.getId(), secondItem.getId()).orElse(null);
	}
	
	/**
	 * Represents the unfinished potion animation identification.
	 */
	private static final Animation ANIMATION = new Animation(363);
	
	/**
	 * Produces finished potions if the player has the requirements required.
	 * @param player {@link #getPlayer()};
	 * @param firstItem the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 * @return <true> if the produce was successful, <false> otherwise.
	 */
	public static boolean produce(Player player, Item firstItem, Item secondItem) {
		FinishedPotion potion = new FinishedPotion(player, firstItem, secondItem);
		if(potion.definition == null) {
			return false;
		}
		potion.start();
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			getPlayer().setNextAnimation(ANIMATION);
			StringBuilder ingrediants = new StringBuilder();
			for (Item ri : definition.requiredItem) {
				ingrediants.append(ri.getDefinitions().getName());
			}
			
			player.getPackets().sendGameMessage("You mix the " + ingrediants + " into your potion.");
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		ObjectList<Item> items = new ObjectArrayList<>();
		items.add(definition.unfinishedPotion);
		items.addAll(Arrays.asList(definition.requiredItem));
		return Optional.of(items.toArray(new Item[items.size()]));
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(definition.equals(FinishedPotionData.OVERLOAD) ? new Item[]{definition.finishedPotion, new Item(229, 4)} : new Item[]{definition.finishedPotion});
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
		return definition.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.HERBLORE;
	}
	
	public boolean canProduce() {
		if(player.getSkills().getLevel(Skills.HERBLORE) < definition.level) {
			getPlayer().getPackets().sendGameMessage("You need a herblore level of " + definition.level + " to register " + TextUtils.appendIndefiniteArticle(definition.toString()) + " potion.");
			return false;
		}
		if (!player.getInventory().containsAny(definition.unfinishedPotion.getId())) {
			player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(definition.unfinishedPotion.getId()).getName() + " to make a " + ItemDefinitions.getItemDefinitions(definition.finishedPotion.getId()).getName() + ".");
			return false;
		}
		return true;
	}
	
	/**
	 * The data required for processing the creation of finished potions.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum FinishedPotionData {
		COCONUT_MILK(5935, 229, 5976, 1, 0),
		ATTACK_POTION(121, 91, 221, 1, 25),
		RANGING_POTION(169, 109, 245, 3, 30),
		MAGIC_POTION(3042, 2483, 3138, 5, 35),
		STRENGTH_POTION(115, 95, 225, 7, 40),
		DEFENCE_POTION(133, 99, 239, 9, 45),
		ANTIPOISON(175, 93, 235, 13, 50),
		RESTORE_POTION(127, 97, 223, 22, 62.5),
		ENERGY_POTION(3010, 97, 1975, 26, 67.5),
		AGILITY_POTION(3034, 3002, 2152, 34, 80),
		COMBAT_POTION(9741, 97, 9736, 36, 84),
		PRAYER_POTION(139, 99, 231, 38, 87.5),
		SUMMONING_POTION(12142, 12181, 12109, 40, 92),
		CRAFTING_POTION(14840, 14856, 5004, 42, 95),
		SUPER_ATTACK(145, 101, 221, 45, 100),
		VIAL_OF_STENCH(18661, 101, 1871, 46, 0),
		SUPER_ANTIPOISON(181, 101, 235, 48, 106.3),
		FISHING_POTION(151, 103, 231, 50, 112.5),
		SUPER_ENERGY(3018, 103, 2970, 52, 117.5),
		HUNTER_POTION(10000, 103, 10109, 53, 120),
		SUPER_STRENGTH(157, 105, 225, 55, 125),
		FLETCHING_POTION(14848, 14856, 11525, 58, 132),
		WEAPON_POISON(187, 105, 241, 60, 137.5),
		SUPER_RESTORE(3026, 3004, 223, 63, 142.5),
		SANFEW_SERUM(10927, 3024, new int[]{235, 1526, 10937}, 65, 160),
		SUPER_DEFENCE(163, 107, 239, 66, 150),
		ANTIFIRE(2454, 2483, 241, 69, 157.5),
		ZAMORAK_BREW(189, 111, 247, 78, 177.5),
		SARADOMIN_BREW(6687, 3002, 6693, 81, 180),
		RECOVER_SPECIAL(15301, 3018, 5972, 84, 200),
		SUPER_ANTIFIRE(15305, 2454, 4621, 85, 210),
		EXTREME_ATTACK(15309, 145, 261, 88, 220),
		EXTREME_STRENGTH(15313, 157, 267, 89, 230),
		EXTREME_DEFENCE(15317, 163, 2481, 90, 240),
		EXTREME_MAGIC(15321, 3042, 9594, 91, 250),
		EXTREME_RANGING(15325, 169, 12539, 92, 260),
		SUPER_PRAYER(15329, 139, 4255, 94, 270),
		OVERLOAD(15332, 269, new int[]{15309, 15313, 15317, 15321, 15325}, 96, 1000);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<FinishedPotionData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(FinishedPotionData.class));
		
		/**
		 * The identification for this finished potion.
		 */
		private final Item finishedPotion;
		
		/**
		 * The identification for this unfinished potion.
		 */
		private final Item unfinishedPotion;
		
		/**
		 * The identification for this required item.
		 */
		private final Item[] requiredItem;
		
		/**
		 * The required level for creating this potion.
		 */
		private final int level;
		
		/**
		 * The identifier which identifies the amount of experience given for this potion.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link FinishedPotionData} enum.
		 * @param finishedPotion {@link #finishedPotion}.
		 * @param unfinishedPotion {@link #unfinishedPotion}.
		 * @param requiredItem {@link #requiredItem}.
		 * @param level {@link #level}.
		 * @param experience {@link #experience}.
		 */
		FinishedPotionData(int finishedPotion, int unfinishedPotion, int[] requiredItem, int level, double experience) {
			this.finishedPotion = new Item(finishedPotion);
			this.unfinishedPotion = new Item(unfinishedPotion);
			this.requiredItem = Item.toList(requiredItem);
			this.level = level;
			this.experience = experience;
		}
		
		/**
		 * Constructs a new {@link FinishedPotionData} enum.
		 * @param finishedPotion {@link #finishedPotion}.
		 * @param unfinishedPotion {@link #unfinishedPotion}.
		 * @param requiredItem {@link #requiredItem}.
		 * @param level {@link #level}.
		 * @param experience {@link #experience}.
		 */
		FinishedPotionData(int finishedPotion, int unfinishedPotion, int requiredItem, int level, double experience) {
			this.finishedPotion = new Item(finishedPotion);
			this.unfinishedPotion = new Item(unfinishedPotion);
			this.requiredItem = new Item[]{new Item(requiredItem)};
			this.level = level;
			this.experience = experience;
		}
		
		@Override
		public final String toString() {
			return name().toLowerCase().replaceAll("_", " ");
		}
		
		/**
		 * Gets the definition for this finished potion.
		 * @return an optional holding the {@link FinishedPotionData} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<FinishedPotionData> getDefinition(int ingredient, int secondIngredient) {
			return VALUES.stream().filter(potion -> potion.unfinishedPotion.getId() == ingredient || potion.unfinishedPotion.getId() == secondIngredient).filter(potion -> Arrays.stream(potion.requiredItem).anyMatch(id -> id.getId() == ingredient || id.getId() == secondIngredient)).findAny();
		}
	}
}
