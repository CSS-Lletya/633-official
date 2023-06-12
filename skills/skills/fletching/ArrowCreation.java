package skills.fletching;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Holds functionality for creating arrows.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ArrowCreation extends ProducingSkillAction {
	
	/**
	 * The definition of this arrow.
	 */
	private final ArrowData definition;
	
	/**
	 * The definition for a headless arrow.
	 */
	private static final Item HEADLESS_ARROW = new Item(53, 15);
	
	/**
	 * Constructs a new {@link ArrowCreation}.
	 * @param player {@link #getPlayer()}
	 * @param definition the definition we're currently handling.
	 */
	private ArrowCreation(Player player, ArrowData definition) {
		super(player, Optional.empty());
		this.definition = definition;
	}
	
	public static boolean fletch(Player player, Item firstItem, Item secondItem) {
		Optional<ArrowData> arrow = ArrowData.getDefinition(firstItem.getId(), secondItem.getId());
		
		if(!arrow.isPresent()) {
			return false;
		}
		
		if(firstItem.getId() == arrow.get().tips.getId() && secondItem.getId() == HEADLESS_ARROW.getId() || firstItem.getId() == HEADLESS_ARROW.getId() && secondItem.getId() == arrow.get().tips.getId()) {
			ArrowCreation creation = new ArrowCreation(player, arrow.get());
			creation.start();
			return true;
		}
		return false;
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(definition.tips.getId(), 15), HEADLESS_ARROW});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(definition.arrow.getId(), 15)});
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
		if(player.getInventory().getAmountOf(definition.tips.getId()) < 15 || player.getInventory().getAmountOf(HEADLESS_ARROW.getId()) < 15) {
			player.getPackets().sendGameMessage("You must have atleast 15 of each item to fletch this arrow.");
			return false;
		}
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			t.cancel();
		}
	}
	
	@Override
	public boolean canExecute() {
		return checkFletching();
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
			getPackets().sendGameMessage("You need a fletching level of " + definition.requirement + " to fletch this arrow.");
			return false;
		}
		return true;
	}
	
	private enum ArrowData {
		BRONZE_ARROWS(39, 882, 1, 39.5),
		OGRE_ARROWS(2865, 2866, 5, 57.0),
		IRON_ARROWS(40, 884, 15, 57.5),
		STEEL_ARROWS(41, 886, 30, 95.0),
		MITHRIL_ARROWS(42, 888, 45, 132.5),
		BROAD_ARROWS(13278, 4160, 52, 225),
		ADAMANT_ARROWS(43, 890, 60, 150),
		RUNE_ARROWS(44, 892, 75, 207.4),
		DRAGON_ARROWS(11237, 11212, 90, 244.5);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<ArrowData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ArrowData.class));
		
		/**
		 * The arrow tip for this arrow.
		 */
		private final Item tips;
		
		/**
		 * The produced item for this arrow.
		 */
		private final Item arrow;
		
		/**
		 * The requirement for this arrow.
		 */
		private final int requirement;
		
		/**
		 * The experience for this arrow.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link ArrowData} enumerator.
		 * @param tips {@link #tips}.
		 * @param arrow {@link #arrow}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 */
		ArrowData(int tips, int arrow, int requirement, double experience) {
			this.tips = new Item(tips);
			this.arrow = new Item(arrow);
			this.requirement = requirement;
			this.experience = experience * 1.70;
		}
		
		public static Optional<ArrowData> getDefinition(int firstItem, int secondItem) {
			return VALUES.stream().filter(def -> def.tips.getId() == firstItem || def.tips.getId() == secondItem).findAny();
		}
	}
	
	public static class HeadlessArrowCreation extends ProducingSkillAction {
		
		private final Item toRemove;
		
		public HeadlessArrowCreation(Player player, Item toRemove) {
			super(player, Optional.empty());
			
			this.toRemove = toRemove;
		}
		
		public static boolean fletchGrenwallSpikes(Player player, Item firstItem, Item secondItem) {
			if(firstItem.getId() == GRENWALL_SPIKES.getId() && secondItem.getId() == FEATHER.getId() || firstItem.getId() == FEATHER.getId() && secondItem.getId() == GRENWALL_SPIKES.getId()) {
				Item toRemove = GRENWALL_SPIKES;
				
				HeadlessArrowCreation creation = new HeadlessArrowCreation(player, toRemove);
				creation.start();
				return true;
			}
			
			return false;
		}
		
		public static boolean fletchArrowShaft(Player player, Item firstItem, Item secondItem) {
			if(firstItem.getId() == ARROW_SHAFT.getId() && secondItem.getId() == FEATHER.getId() || firstItem.getId() == FEATHER.getId() && secondItem.getId() == ARROW_SHAFT.getId()) {
				Item toRemove = ARROW_SHAFT;
				
				HeadlessArrowCreation creation = new HeadlessArrowCreation(player, toRemove);
				creation.start();
				return true;
			}
			
			return false;
		}
		
		/**
		 * The definition for arrow shafts.
		 */
		private static final Item ARROW_SHAFT = new Item(52, 15);
		
		/**
		 * The definition for grenwall spikes
		 */
		private static final Item GRENWALL_SPIKES = new Item(12539, 15);
		
		/**
		 * The definition for a feather.
		 */
		private static final Item FEATHER = new Item(314, 15);
		
		@Override
		public Optional<Item[]> removeItem() {
			return Optional.of(new Item[]{toRemove, FEATHER});
		}
		
		@Override
		public Optional<Item[]> produceItem() {
			return Optional.of(new Item[]{new Item(HEADLESS_ARROW.getId(), 15)});
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
			if(player.getInventory().getNumberOf(toRemove.getId()) < 15 || player.getInventory().getNumberOf(FEATHER.getId()) < 15) {
				getPackets().sendGameMessage("You must have atleast 15 of each item to fletch a headless arrow.");
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
			return 15;
		}
		
		@Override
		public int getSkillId() {
			return Skills.FLETCHING;
		}
	}
	
}
