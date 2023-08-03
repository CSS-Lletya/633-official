package skills.crafting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import lombok.AllArgsConstructor;
import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Represents a Player casting items from silver bars into mouls to create end product
 * @author Dennis
 *
 */
public final class SilverItemCasting extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final SilverProduct data;
	
	/**
	 * The amount of times this task should run for.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link SilverItemCasting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 * @param packetId {@link #amount}.
	 */
	public SilverItemCasting(Player player, SilverProduct data, int packetId, int inputAmount) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = (inputAmount == 0 ? getAmount(packetId) : inputAmount);
	}
	
	/**
	 * Get amount based off of packetId read
	 * @param packetId
	 * @return
	 */
	private static int getAmount(int packetId) {
		return packetId == 11 ? 1 : packetId == 29 ? 5 : packetId == 31 ? 10 : packetId == 32 ? Integer.MAX_VALUE : 0;
	}
	
	/**
	 * Sends the interface
	 * @param player
	 */
	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(438);
		SilverProduct.VALUES.stream().filter(mould -> player.getInventory().containsAny(mould.item))
		.forEach(mould -> player.getPackets().sendItemOnIComponent(438, mould.itemComponentId +2 , mould.product, 1));
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			amount--;
			if(amount <= 0)
				t.cancel();
		}
	}
	public static SilverProduct getType(int comp) {
		return comp == 16 ? SilverProduct.SARA_SYMBOL : comp == 30 ? SilverProduct.SILVER_SICKLE : comp == 44 ? SilverProduct.TIARA : comp == 59 ?
				SilverProduct.DEMONIC_SIGIL : comp == 23 ? SilverProduct.ZAMORAK_SYMBOL : comp == 37 ? SilverProduct.LIGHTNING_ROD : SilverProduct.SILVER_CROSSBOW_BOLTS;
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.SMELTING_INSIDE_FURNACE);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(ItemNames.SILVER_BAR_2355)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(data.product)});
	}
	
	@Override
	public int delay() {
		return 5;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		player.getInterfaceManager().closeInterfaces();
		return checkCrafting();
	}
	
	@Override
	public boolean canExecute() {
		return checkCrafting();
	}
	
	@Override
	public double experience() {
		return data.xp;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	@Override
	public void onStop() {
	}
	
	private boolean checkCrafting() {
		if(player.getSkills().getLevel(Skills.CRAFTING) < data.requirement) {
			player.getPackets().sendGameMessage("You need a crafting level of " + data.requirement + " to create " + TextUtils.appendIndefiniteArticle(ItemDefinitions.getItemDefinitions(data.item).getName()));
			return false;
		}
		if (!player.getInventory().containsAny(data.item)) {
			player.getPackets().sendGameMessage("You do not have the right mould to create this.");
			return false;
		}
		return true;
	}
	
	@AllArgsConstructor
	public enum SilverProduct {
		SARA_SYMBOL(16, 1599, 1716, 16, 20, 15),
		SILVER_SICKLE(30, 2976, 2961, 18, 50, 29),
		TIARA(44, 5523, 5525,23, 52.5, 43),
		DEMONIC_SIGIL(59, 6747, 6748,20,50, 58),
//		SILVTHRIL_CHAIN(73, 13153, 13154,20,57, 72),//not supported
		ZAMORAK_SYMBOL(23, 1594, 1722,17,50,22),
		LIGHTNING_ROD(37, 4200, 4201,20, 50, 36),
//		SILVTHRIL_ROD(52, 1749, 2507,20, 73, 51),//not supported
		SILVER_CROSSBOW_BOLTS(66, 9434, 9382,21, 50, 65),
//		SILVER_KEY(79, 1749, 2507,20, 73, 78),//not supported
		
		;

		@SuppressWarnings("unused")
		private final int button;

		private final int item;

		private final int product;
		
		private final int requirement;
		
		private final double xp;
		
		private final int itemComponentId;

		public static final ImmutableSet<SilverProduct> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SilverProduct.class));
	}
}