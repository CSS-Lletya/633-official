package skills.fletching;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Holds functionality for stringing crossbows.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CrossbowStringing extends ProducingSkillAction {
	
	/**
	 * The definition of this bow.
	 */
	private final StringingData definition;
	
	/**
	 * Constructs a new {@link CrossbowStringing}.
	 * @param player {@link #getPlayer()}
	 * @param definition the definition we're currently handling.
	 */
	public CrossbowStringing(Player player, StringingData definition) {
		super(player, Optional.empty());
		
		this.definition = definition;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.setNextAnimation(definition.animation);
		}
	}
	
	public static boolean string(Player player, Item firstItem, Item secondItem) {
		Optional<StringingData> bow = StringingData.getDefinition(firstItem.getId(), secondItem.getId());
		
		if(!bow.isPresent()) {
			return false;
		}
		
		if(firstItem.getId() == bow.get().unstrung.getId() && secondItem.getId() == 9438 || firstItem.getId() == 9438 && secondItem.getId() == bow.get().unstrung.getId()) {
			CrossbowStringing fletching = new CrossbowStringing(player, bow.get());
			fletching.start();
			return true;
		}
		return false;
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.unstrung, new Item(9438)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.strung});
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
			player.getPackets().sendGameMessage("You need a fletching level of " + definition.requirement + " to string this bow.");
			return false;
		}
		return true;
	}
	
	private enum StringingData {
		BRONZE_CROSSBOW(9454, 9174, 9, 6.0, 6671),
		BLURITE_CROSSBOW(9456, 9176, 24, 16.0, 6672),
		IRON_CROSSBOW(9457, 9177, 39, 22.0, 6673),
		STEEL_CROSSBOW(9459, 9179, 46, 27.0, 6674),
		MITHRIL_CROSSBOW(9461, 9181, 54, 32.0, 6675),
		ADAMANT_CROSSBOW(9463, 9183, 61, 41.0, 6676),
		RUNITE_CROSSBOW(9465, 9185, 69, 50.0, 6677);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<StringingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(StringingData.class));
		
		/**
		 * The item for the unstrung bow.
		 */
		private final Item unstrung;
		
		/**
		 * The item for the strung bow.
		 */
		private final Item strung;
		
		/**
		 * The requirement for stringing this bow.
		 */
		private final int requirement;
		
		/**
		 * The experience gained upon stringing this bow.
		 */
		private final double experience;
		
		/**
		 * The animation performed upon stringing this bow.
		 */
		private final Animation animation;
		
		/**
		 * Constructs a new {@link StringingData} enumerator.
		 * @param unstrung {@link #unstrung}.
		 * @param strung {@link #strung}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param animation {@link #animation}.
		 */
		StringingData(int unstrung, int strung, int requirement, double experience, int animation) {
			this.unstrung = new Item(unstrung);
			this.strung = new Item(strung);
			this.requirement = requirement;
			this.experience = experience * 1.70;
			this.animation = new Animation(animation);
		}
		
		public static Optional<StringingData> getDefinition(int firstItem, int secondItem) {
			return VALUES.stream().filter(def -> def.unstrung.getId() == firstItem || def.unstrung.getId() == secondItem).findAny();
		}
	}
	
}
