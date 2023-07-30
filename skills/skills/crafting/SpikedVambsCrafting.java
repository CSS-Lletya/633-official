package skills.crafting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import lombok.AllArgsConstructor;
import skills.ProducingSkillAction;
import skills.Skills;

public class SpikedVambsCrafting extends ProducingSkillAction {
	
	/**
	 * The staff data this skill action is dependent of.
	 */
	private final SpikedData data;
	
	/**
	 * Constructs a new {@link SpikedVambsCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public SpikedVambsCrafting(Player player, SpikedData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	/**
	 * A constant representing the ball of wool item.
	 */
	private static final Item KEBBIT_CLAWS = new Item(10113);
	
	/**
	 * Attempts to start stringing any amulets.
	 * @param player the player attempting the skill action.
	 * @param used the item used.
	 * @param usedOn the item that got used on.
	 * @return {@code true} if the skill action started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn) {
		SpikedData data = SpikedData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		create(player, data);
		return true;
	}
	
	public static void create(Player player, SpikedData data) {
		SpikedVambsCrafting crafting = new SpikedVambsCrafting(player, data);
		crafting.start();
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.base, KEBBIT_CLAWS});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.product});
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(Animations.RUB_HANDS_TOGETHER);
	}
	
	@Override
	public int delay() {
		return 2;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		return true;
	}
	
	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.requiredLevel) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of " + data.requiredLevel + " to continue this action.");
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		return SpikedData.getDefinition(player).get().experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used
	 * to string amulets.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	@AllArgsConstructor
	public enum SpikedData {
		REGULAR( new Item(1063), new Item(10077), 32, 6),
		GREEN( new Item(1065), new Item(10079), 32, 6),
		BLUE( new Item(2487), new Item(10081), 32, 6),
		RED( new Item(2489), new Item(10083), 32, 6),
		BLACK( new Item(2491), new Item(10085), 32, 6);
		
		/**
		 * The item which needs to be stringed.
		 */
		public final Item base;
		
		/**
		 * The item which is stringed.
		 */
		public final Item product;
		
		public final int requiredLevel;
		
		public final double experience;
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<SpikedData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SpikedData.class));
		
		public static Optional<SpikedData> getDefinition(Player player) {
			return VALUES.stream().filter($it -> player.getInventory().containsAny($it.base.getId())).findAny();
		}
		
		public static Optional<SpikedData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.base.getId() == itemUsed || $it.base.getId() == usedOn).filter($it -> KEBBIT_CLAWS.getId() == itemUsed || KEBBIT_CLAWS.getId() == usedOn).findAny();
		}
	}
}