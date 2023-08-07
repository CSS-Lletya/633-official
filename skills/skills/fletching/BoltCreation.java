package skills.fletching;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Holds functionality for creating bolts.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BoltCreation extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final BoltData data;
	
	/**
	 * Constructs a new {@link BoltCreation}.
	 * @param player {@link #getPlayer()}.
	 */
	public BoltCreation(Player player, BoltData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	/**
	 * Attempts to start this skill action.
	 * @param player the player attempting to start the skill action.
	 * @param used the item used on the usedOn.
	 * @param usedOn the item that was used by the used.
	 * @return {@code true} if the skill action submitted, {@code false} otherwise.
	 */
	public static boolean fletch(Player player, Item used, Item usedOn) {
		BoltData data = BoltData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		BoltCreation fletching = new BoltCreation(player, data);
		fletching.start();
		return true;
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(data.unfinished.getId(), 15), new Item(data.required.getId(), 15)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(data.product.getId(), 15)});
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
	public void onProduce(Task t, boolean success) {
		if(success)
			player.setNextAnimation(Animations.BOLT_CREATION);
	}
	
	@Override
	public boolean initialize() {
		if(!checkFletching()) {
			return false;
		}
		
		if(player.getInventory().getAmountOf(data.unfinished.getId()) < 15 || player.getInventory().getAmountOf(data.required.getId()) < 15) {
			player.getPackets().sendGameMessage("You must have atleast 15 of each item to fletch this bolt.");
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return checkFletching();
	}
	
	@Override
	public double experience() {
		return data.experience;
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(6702));
	}
	
	@Override
	public int getSkillId() {
		return Skills.FLETCHING;
	}
	
	private boolean checkFletching() {
		if(player.getSkills().getLevel(Skills.FLETCHING) < data.requirement) {
			getPackets().sendGameMessage("You need a Fletching level of " + data.requirement + " to continue this action.");
			return false;
		}
		if (!player.getInventory().containsAny(data.unfinished.getId())) {
			player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(data.unfinished.getId()).getName() + " to make a " + ItemDefinitions.getItemDefinitions(data.product.getId()).getName() + ".");
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent the constants used to
	 * register bolts with.
	 * @author <a href="http://www.rune-server.org/members/Golang/">Jay</a>
	 */
	private enum BoltData {
		BRONZE_BOLTS(9375, 314, 877, 9, 5),
		OPAL_BOLTS(877, 45, 879, 11, 16),
		BLURITE_BOLTS(9376, 314, 9139, 24, 10),
		JADE_BOLTS(9139, 9187, 9335, 26, 24),
		IRON_BOLTS(9377, 314, 9140, 39, 15),
		PEARL_BOLTS(9140, 46, 880, 41, 32),
		SILVER_BOLTS(9382, 314, 9145, 43, 25),
		STEEL_BOLTS(9378, 314, 9141, 46, 35),
		RED_TOPAZ_BOLTS(9141, 9188, 9336, 48, 39),
		BARBED_BOLTS(877, 47, 881, 51, 95),
		MITHRIL_BOLTS(9379, 314, 9142, 54, 50),
		BROAD_TIPPED_BOLTS(13279, 314, 4172, 55, 30),
		SAPPHIRE_BOLTS(9142, 9189, 9337, 56, 47),
		EMERALD_BOLTS(9142, 9190, 9338, 58, 55),
		ADAMANT_BOLTS(9380, 314, 9143, 61, 70),
		RUBY_BOLTS(9143, 9191, 9339, 63, 63),
		DIAMOND_BOLTS(9143, 9192, 9340, 65, 70),
		RUNITE_BOLTS(9381, 314, 9144, 69, 100),
		DRAGON_BOLTS(9144, 9193, 9341, 71, 82),
		ONYX_BOLTS(9144, 9194, 9342, 73, 94),
		ABYSSALBANE_BOLTS(21858, 314, 21675, 80, 100),
		BASILISKBANE_BOLTS(21848, 314, 21670, 80, 100),
		DRAGONBANE_BOLTS(21843, 314, 21660, 80, 100),
		WALLASALKIBANE_BOLTS(21853, 314, 21665, 80, 100);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<BoltData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BoltData.class));
		
		/**
		 * The unfinished bolt.
		 */
		private final Item unfinished;
		
		/**
		 * The required item.
		 */
		private final Item required;
		
		/**
		 * The finished product.
		 */
		private final Item product;
		
		/**
		 * The requirement required.
		 */
		private final int requirement;
		
		/**
		 * The experience gained.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link BoltData}.
		 * @param unfinished {@link #unfinished}.
		 * @param required {@link #required}.
		 * @param product {@link #product}.
		 * @param requirement {@link #required}.
		 * @param experience {@link #experience}.
		 */
		BoltData(int unfinished, int required, int product, int requirement, double experience) {
			this.unfinished = new Item(unfinished);
			this.required = new Item(required);
			this.product = new Item(product);
			this.requirement = requirement;
			this.experience = experience * 1.70;
		}
		
		public static Optional<BoltData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.unfinished.getId() == itemUsed || $it.unfinished.getId() == usedOn).filter($it -> $it.required.getId() == itemUsed || $it.required.getId() == usedOn).findAny();
		}
	}
}
