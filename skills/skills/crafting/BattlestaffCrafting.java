package skills.crafting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import lombok.AllArgsConstructor;
import skills.ProducingSkillAction;
import skills.Skills;

public final class BattlestaffCrafting extends ProducingSkillAction {
	
	/**
	 * The staff data this skill action is dependent of.
	 */
	private final StaffData data;
	
	/**
	 * Constructs a new {@link BattlestaffCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public BattlestaffCrafting(Player player, StaffData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	/**
	 * A constant representing the ball of wool item.
	 */
	private static final Item BATTLESTAFF = new Item(ItemNames.BATTLESTAFF_1391);
	
	/**
	 * Attempts to start stringing any amulets.
	 * @param player the player attempting the skill action.
	 * @param used the item used.
	 * @param usedOn the item that got used on.
	 * @return {@code true} if the skill action started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn) {
		StaffData data = StaffData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		create(player, data);
		return true;
	}
	
	public static void create(Player player, StaffData data) {
		BattlestaffCrafting crafting = new BattlestaffCrafting(player, data);
		crafting.start();
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.setNextAnimation(new Animation(4412));
			player.setNextGraphics(new Graphics(728));
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.orb, new Item(ItemNames.BATTLESTAFF_1391)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.product});
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
		return StaffData.getDefinition(player).get().experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	@AllArgsConstructor
	public enum StaffData {
		FIRE( new Item(569), new Item(1393), 62, 125),
		WATER( new Item(571), new Item(1395), 54, 100),
		EARTH( new Item(575), new Item(1399), 58, 112.5),
		AIR( new Item(573), new Item(1397), 66, 137.5);
		
		/**
		 * The item which needs to be stringed.
		 */
		public final Item orb;
		
		/**
		 * The item which is stringed.
		 */
		public final Item product;
		
		public final int requiredLevel;
		
		public final double experience;
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<StaffData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(StaffData.class));
		
		public static Optional<StaffData> getDefinition(Player player) {
			return VALUES.stream().filter($it -> player.getInventory().containsAny($it.orb.getId())).findAny();
		}
		
		public static Optional<StaffData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.orb.getId() == itemUsed || $it.orb.getId() == usedOn).filter($it -> BATTLESTAFF.getId() == itemUsed || BATTLESTAFF.getId() == usedOn).findAny();
		}
	}
}