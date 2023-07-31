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

public class SnailHelmCrafting extends ProducingSkillAction {
	
	/**
	 * The staff data this skill action is dependent of.
	 */
	private final SnelmData data;
	
	/**
	 * Constructs a new {@link SnailHelmCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public SnailHelmCrafting(Player player, SnelmData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	/**
	 * A constant representing the ball of wool item.
	 */
	private static final Item CHISEL = new Item(1755);
	
	/**
	 * Attempts to start stringing any amulets.
	 * @param player the player attempting the skill action.
	 * @param used the item used.
	 * @param usedOn the item that got used on.
	 * @return {@code true} if the skill action started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn) {
		SnelmData data = SnelmData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		create(player, data);
		return true;
	}
	
	public static void create(Player player, SnelmData data) {
		SnailHelmCrafting crafting = new SnailHelmCrafting(player, data);
		crafting.start();
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.base});
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
		return true;
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
		return SnelmData.getDefinition(player).get().experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	@AllArgsConstructor
	public enum SnelmData {
		OCHRE( new Item(3349), new Item(3341), 15, 32.5),
		BLOOD( new Item(3347), new Item(3339), 15, 32.5),
		MYRE( new Item(3345), new Item(3337), 15, 32.5),
		BARK( new Item(3353), new Item(3335), 15, 32.5),
		BLUE( new Item(3351), new Item(3343), 15, 32.5);
		
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
		private static final ImmutableSet<SnelmData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SnelmData.class));
		
		public static Optional<SnelmData> getDefinition(Player player) {
			return VALUES.stream().filter($it -> player.getInventory().containsAny($it.base.getId())).findAny();
		}
		
		public static Optional<SnelmData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.base.getId() == itemUsed || $it.base.getId() == usedOn).filter($it -> CHISEL.getId() == itemUsed || CHISEL.getId() == usedOn).findAny();
		}
	}
}