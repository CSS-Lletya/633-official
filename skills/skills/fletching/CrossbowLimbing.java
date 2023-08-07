package skills.fletching;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Holds functionality for constructing crossbows with their respective limbs.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CrossbowLimbing extends ProducingSkillAction {
	
	/**
	 * The definition of this bow.
	 */
	private final CrossbowData definition;
	
	/**
	 * Constructs a new {@link CrossbowLimbing}.
	 * @param player {@link #getPlayer()}
	 * @param definition the definition we're currently handling.
	 */
	public CrossbowLimbing(Player player, CrossbowData definition) {
		super(player, Optional.empty());
		
		this.definition = definition;
	}
	
	public static boolean construct(Player player, Item firstItem, Item secondItem) {
		Optional<CrossbowData> bow = CrossbowData.getDefinition(firstItem.getId(), secondItem.getId());
		
		if(!bow.isPresent()) {
			return false;
		}
		
		if(firstItem.getId() == bow.get().limb.getId() && secondItem.getId() == bow.get().stock.getId() || firstItem.getId() == bow.get().stock.getId() && secondItem.getId() == bow.get().limb.getId()) {
			CrossbowLimbing fletching = new CrossbowLimbing(player, bow.get());
			fletching.start();
			return true;
		}
		return false;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(definition.animation);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.stock, definition.limb});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.crossbow_u});
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
		if(!checkFletching()) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExecute() {
		if(!checkFletching()) {
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
		return Skills.FLETCHING;
	}
	
	private boolean checkFletching() {
		if(player.getSkills().getLevel(Skills.FLETCHING) < definition.requirement) {
			player.getPackets().sendGameMessage("You need a fletching level of " + definition.requirement + " to add a limb to this stock.");
			return false;
		}
		if (!player.getInventory().containsAny(definition.stock.getId())) {
			player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(definition.stock.getId()).getName() + " to make a " + ItemDefinitions.getItemDefinitions(definition.crossbow_u.getId()).getName() + ".");
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represents the data required for limbing crossbows.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum CrossbowData {
		BRONZE_CROSSBOW(9440, 9454, 9420, 9, 12.0, 4436),
		BLURITE_CROSSBOW(9442, 9456, 9422, 24, 32.0, 4437),
		IRON_CROSSBOW(9444, 9457, 9423, 39, 44.0, 4438),
		STEEL_CROSSBOW(9446, 9459, 9425, 46, 54.0, 4439),
		MITHRIL_CROSSBOW(9448, 9461, 9427, 54, 64.0, 4440),
		ADAMANT_CROSSBOW(9450, 9463, 9429, 61, 82.0, 4441),
		RUNITE_CROSSBOW(9452, 9465, 9431, 69, 100.0, 4442);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<CrossbowData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(CrossbowData.class));
		
		/**
		 * The stock for this crossbow.
		 */
		private final Item stock;
		
		/**
		 * The limb for this crossbow.
		 */
		private final Item limb;
		
		/**
		 * The crossbow(u) which is produced.
		 */
		private final Item crossbow_u;
		
		/**
		 * The requirement for this crossbow.
		 */
		private final int requirement;
		
		/**
		 * The experience for this crossbow.
		 */
		private final double experience;
		
		/**
		 * The animation for this crossbow.
		 */
		private final Animation animation;
		
		/**
		 * Constructs a new {@link CrossbowData} enumerator.
		 * @param stock {@link #stock}.
		 * @param limb {@link #limb}.
		 * @param crossbow_u {@link #crossbow_u}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param animation {@link #animation}.
		 */
		CrossbowData(int stock, int crossbow_u, int limb, int requirement, double experience, int animation) {
			this.stock = new Item(stock);
			this.limb = new Item(limb);
			this.crossbow_u = new Item(crossbow_u);
			this.requirement = requirement;
			this.experience = experience * 1.70;
			this.animation = new Animation(animation);
		}
		
		public static Optional<CrossbowData> getDefinition(int firstItem, int secondItem) {
			return VALUES.stream().filter(def -> def.stock.getId() == firstItem || def.stock.getId() == secondItem).findAny();
		}
	}
}
