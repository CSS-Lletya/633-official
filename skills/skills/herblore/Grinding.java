package skills.herblore;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.ProducingSkillAction;
import skills.SkillHandler;
import skills.Skills;

/**
 * Represents the procession for grinding items.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Grinding extends ProducingSkillAction {
	
	/**
	 * The {@link GrindingData} holding all the data required for processing
	 * the creation of grindable items.
	 */
	private final GrindingData definition;
	
	/**
	 * Represents the identifier for the pestle and mortar.
	 */
	private static final Item PESTLE_MORTAR = new Item(233);
	
	/**
	 * Represents the animation for tar creation.
	 */
	private static final Animation ANIMATION = new Animation(364);
	
	/**
	 * Constructs a new {@link Grinding}.
	 */
	public Grinding(Player player, Item firstItem, Item secondItem) {
		super(player, Optional.of(player));
		Item item = firstItem.getId() == PESTLE_MORTAR.getId() ? secondItem : firstItem;
		this.definition = GrindingData.getDefinition(item.getId()).orElse(null);
	}
	
	/**
	 * Produces guam tars if the player has the requirements required.
	 * @param player {@link #getPlayer()};
	 * @param firstItem the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 * @return <true> if the produce was successful, <false> otherwise.
	 */
	public static boolean produce(Player player, Item firstItem, Item secondItem) {
		Grinding grinding = new Grinding(player, firstItem, secondItem);
		if(grinding.definition == null) {
			return false;
		}
		if(firstItem.getId() == PESTLE_MORTAR.getId() && secondItem.getId() == grinding.definition.item.getId() || firstItem.getId() == grinding.definition.item.getId() && secondItem.getId() == PESTLE_MORTAR.getId()) {
			grinding.start();
			player.getAudioManager().sendSound(Sounds.PESTLE_AND_MORTAR_GRINDING);
			return true;
		}
		return false;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			getPlayer().setNextAnimation(ANIMATION);
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.item});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.product});
	}
	
	@Override
	public int delay() {
		return 4;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean initialize() {
		return checkGrinding();
	}
	
	@Override
	public boolean canExecute() {
		return checkGrinding();
	}
	
	@Override
	public void onSkillAction(SkillHandler other) {
		if(other instanceof Grinding) {
			this.stop();
		}
	}
	
	@Override
	public double experience() {
		return definition.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.HERBLORE;
	}
	
	private boolean checkGrinding() {
		if(definition == null) {
			return false;
		}
		if(!getPlayer().getInventory().containsAny(PESTLE_MORTAR.getId())) {
			getPlayer().getPackets().sendGameMessage("You need a pestle and mortar to do this.");
			return false;
		}
		if (!player.getInventory().containsAny(definition.item.getId())) {
			player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(definition.item.getId()).getName() + " to make a " + ItemDefinitions.getItemDefinitions(definition.product.getId()).getName() + ".");
			return false;
		}
		return true;
	}
	
	/**
	 * The data required for processing the creation of grindable items.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum GrindingData {
		UNICORN_HORN(237, 235, 20.0),
		CHOCOLATE_BAR(1973, 1975, 30),
		NEST(5075, 6693, 45),
		KEBBIT_TEETH(10109, 10111, 50),
		BLUE_DRAGON_SCALE(243, 241, 65),
		DIAMOND_ROOT(14703, 14704, 75),
		DESERT_GOAT_HORN(9735, 9736, 80),
		RUNE_SHARDS(6466, 6467, 90),
		MUD_RUNE(4698, 9594, 100),
		ASHES(592, 8865, 110),
		SEAWEED(401, 6683, 120),
		EDIBLE_SEAWEED(403, 6683, 130),
		BAT_BONES(530, 2391, 145),
		CHARCOAL(973, 704, 160),
		RAW_COD(341, 7528, 170),
		KELP(7516, 7517, 190),
		CRAB_MEAT(7518, 7527, 200),
		ASTRAL_RUNE_SHARDS(11156, 11155, 215),
		SUQAH_TOOTH(9079, 9082, 230),
		DRIED_THISTLE(3263, 3264, 240),
		GARLIC(1550, 4698, 255),
		BLACK_MUSHROOM(4620, 4622, 280);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<GrindingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GrindingData.class));
		
		/**
		 * The identification for the producible item.
		 */
		private final Item item;
		
		/**
		 * The identification for the final product.
		 */
		private final Item product;
		
		/**
		 * The experience identification for the final product.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link GrindingData} enumerator.
		 * @param item {@link #item}.
		 * @param product {@link #product}.
		 * @param experience {@link #experience}.
		 */
		GrindingData(int item, int product, double experience) {
			this.item = new Item(item);
			this.product = new Item(product);
			this.experience = experience;
		}
		
		@Override
		public final String toString() {
			return name().toLowerCase().replaceAll("_", " ");
		}
		
		/**
		 * Gets the definition for this guam tar.
		 * @param identifier the identifier to check for.
		 * @return an optional holding the {@link GuamTar} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<GrindingData> getDefinition(int identifier) {
			return VALUES.stream().filter(def -> def.item.getId() == identifier).findAny();
		}
	}
}