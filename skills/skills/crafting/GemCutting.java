package skills.crafting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

import skills.ProducingSkillAction;
import skills.Skills;

public class GemCutting extends ProducingSkillAction {

	/**
	 * The definition we're currently creating items for.
	 */
	private final GemCraftingData data;

	/**
	 * Determines if the underlying skill action is a fletching skill.
	 */
	private final boolean fletching;

	/**
	 * Constructs a new {@link GemCutting}.
	 * 
	 * @param player    {@link #getPlayer()}.
	 * @param data      {@link #data}.
	 * @param fletching {@link #fletching}.
	 */
	public GemCutting(Player player, GemCraftingData data) {
		super(player, Optional.empty());
		this.data = data;
		this.fletching = data.fletching;
	}

	/**
	 * Attempts to cut the gem for the specified {@code player}.
	 * 
	 * @param player {@link #getPlayer()}.
	 * @param item   the item used.
	 * @param item2  the item used on.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static boolean cut(Player player, Item item, Item item2) {
		Optional<GemCraftingData> data = GemCraftingData.getDefinition(item.getId(), item2.getId());

		if (!data.isPresent()) {
			return false;
		}

		GemCutting action = new GemCutting(player, data.get());
		action.start();
		return true;
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onProduce(Task t, boolean success) {
		player.getAudioManager().sendSound(Sounds.CHISELING);
	}

	@Override
	public boolean canExecute() {
		return checkCrafting();
	}

	@Override
	public boolean initialize() {
		return checkCrafting();
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[] { data.gem });
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[] { (RandomUtils.success(calculateCrushingChance(player.getSkills().getLevel(Skills.CRAFTING)))) ? new Item(ItemNames.CRUSHED_GEM_1633) :  data.produce });
	}

	@Override
	public double experience() {
		return data.experience;
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
	public Optional<Animation> startAnimation() {
		return Optional.of(data.animation);
	}

	@Override
	public int getSkillId() {
		return fletching ? Skills.FLETCHING : Skills.CRAFTING;
	}

	private boolean checkCrafting() {
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.requirement) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of " + data.requirement + " to continue this action.");
			return false;
		}
		return true;
	}

	public static double calculateCrushingChance(int craftingLevel) {
        double baseChance = 0.50;
        double maxLevel = 99.0;
        double chance = baseChance - ((baseChance - 0.02) * (craftingLevel - 1) / (maxLevel - 1));
        return chance;
    }

	public enum GemCraftingData {
		OPAL(new Item(ItemNames.UNCUT_OPAL_1625), new Item(ItemNames.OPAL_1609), 1, 15, 891),
		JADE(new Item(ItemNames.UNCUT_JADE_1627), new Item(ItemNames.JADE_1611), 13, 20, 891),
		RED_TOPAZ(new Item(ItemNames.UNCUT_RED_TOPAZ_1629), new Item(ItemNames.RED_TOPAZ_1613), 16, 25, 892),
		SAPPHIRE(new Item(ItemNames.UNCUT_SAPPHIRE_1623), new Item(ItemNames.SAPPHIRE_1607), 20, 50, 888),
		EMERALD(new Item(ItemNames.UNCUT_EMERALD_1621), new Item(ItemNames.EMERALD_1605), 27, 67.5, 889),
		RUBY(new Item(ItemNames.UNCUT_RUBY_1619), new Item(ItemNames.RUBY_1603), 34, 85, 887),
		DIAMOND(new Item(ItemNames.UNCUT_DIAMOND_1617), new Item(ItemNames.DIAMOND_1601), 43, 107.5, 890),
		DRAGONSTONE(new Item(ItemNames.UNCUT_DRAGONSTONE_1631), new Item(ItemNames.DRAGONSTONE_1615), 55, 137.5, 890),
		ONYX(new Item(ItemNames.UNCUT_ONYX_6571), new Item(ItemNames.ONYX_6573), 67, 167.5, 2717),
		OPAL_BOLT_TIPS(new Item(ItemNames.OPAL_1609), new Item(ItemNames.OPAL_BOLT_TIPS_45, 12), 11, 1.5, 6702, true),
		JADE_BOLT_TIPS(new Item(ItemNames.JADE_1611), new Item(ItemNames.JADE_BOLT_TIPS_9187, 12), 26, 2.0, 6702, true),
		PEARL_BOLT_TIPS(new Item(ItemNames.OYSTER_PEARL_411), new Item(ItemNames.PEARL_BOLT_TIPS_46, 6), 41, 3.2, 6702,true),
		PEARL_BOLT_TIPS_2(new Item(ItemNames.OYSTER_PEARLS_413), new Item(ItemNames.PEARL_BOLT_TIPS_46, 24), 41, 3.2, 6702, true),
		TOPAZ_BOLT_TIPS(new Item(ItemNames.RED_TOPAZ_1613), new Item(ItemNames.TOPAZ_BOLT_TIPS_9188, 12), 48, 3.9, 6702, true),
		SAPPHIRE_BOLT_TIPS(new Item(ItemNames.SAPPHIRE_1607), new Item(ItemNames.SAPPHIRE_BOLT_TIPS_9189, 12), 56, 4.0, 6702, true),
		EMERALD_BOLT_TIPS(new Item(ItemNames.EMERALD_1605), new Item(ItemNames.EMERALD_BOLT_TIPS_9190, 12), 58, 5.5, 6702, true),
		RUBY_BOLT_TIPS(new Item(ItemNames.RUBY_1603), new Item(ItemNames.RUBY_BOLT_TIPS_9191, 12), 63, 6.3, 6702, true),
		DIAMOND_BOLT_TIPS(new Item(ItemNames.DIAMOND_1601), new Item(ItemNames.DIAMOND_BOLT_TIPS_9192, 12), 65, 7, 6702, true),
		DRAGONSTONE_BOLT_TIPS(new Item(ItemNames.DRAGONSTONE_1615), new Item(ItemNames.DRAGON_BOLT_TIPS_9193, 12), 71, 8.2, 6702, true),
		ONYX_BOLT_TIPS(new Item(ItemNames.ONYX_6573), new Item(ItemNames.ONYX_BOLT_TIPS_9194, 24), 73, 9.4, 6702, true),
		KEBBIT_BOLT(new Item(ItemNames.KEBBIT_9953), new Item(ItemNames.KEBBIT_BOLTS_10158, 6), 32, 5.8, 6702, true),
		LONG_KEBBIT_BOLT(new Item(ItemNames.LONG_KEBBIT_SPIKE_10107), new Item(ItemNames.LONG_KEBBIT_BOLTS_10159, 6), 83, 7.89, 6702, true)
		;

		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<GemCraftingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GemCraftingData.class));

		/**
		 * The item required to register the produced form of.
		 */
		public final Item gem;

		/**
		 * The produced item.
		 */
		public final Item produce;

		/**
		 * The requirement for cutting this gem.
		 */
		private final int requirement;

		/**
		 * The experience gained upon cutting this gem.
		 */
		private final double experience;

		/**
		 * The animation for cutting this gem.
		 */
		private final Animation animation;

		/**
		 * Determines if this is a fletching skill action.
		 */
		private final boolean fletching;

		/**
		 * Constructs a new {@link GemCraftingData}.
		 * 
		 * @param gem         {@link #gem}.
		 * @param produce     {@link #produce}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param animationId {@link #animation}.
		 * @param fletching   {@link #fletching}.
		 */
		GemCraftingData(Item gem, Item produce, int requirement, double experience, int animationId,
				boolean fletching) {
			this.gem = gem;
			this.produce = produce;
			this.requirement = requirement;
			this.experience = experience;
			this.animation = new Animation(animationId);
			this.fletching = fletching;
		}

		/**
		 * Constructs a new {@link GemCraftingData}.
		 * 
		 * @param gem         {@link #gem}.
		 * @param produce     {@link #produce}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param animationId {@link #animation}.
		 */
		GemCraftingData(Item gem, Item produce, int requirement, double experience, int animationId) {
			this.gem = gem;
			this.produce = produce;
			this.requirement = requirement;
			this.experience = experience;
			this.animation = new Animation(animationId);
			this.fletching = false;
		}

		public static Optional<GemCraftingData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.gem.getId() == itemUsed || $it.gem.getId() == usedOn)
					.filter($it -> ItemNames.CHISEL_1755 == itemUsed || ItemNames.CHISEL_1755 == usedOn).findAny();
		}
	}
}
