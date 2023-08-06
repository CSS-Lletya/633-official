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
import com.rs.utilities.RandomUtility;
import com.rs.utilities.TextUtils;

import lombok.AllArgsConstructor;
import skills.ProducingSkillAction;
import skills.Skills;

public final class LeatherItemCrafting extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final LeatherProduct data;
	
	/**
	 * The amount of times this task should run for.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link LeatherItemCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 * @param packetId {@link #amount}.
	 */
	public LeatherItemCrafting(Player player, LeatherProduct data, int packetId, int inputAmount) {
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
	
	public static void handleThreadRemoval(Player player) {
		player.getDetails().getThreadsUsed().getAndDecrement();
		if (player.getDetails().getThreadsUsed().get() == 0) {
			player.getDetails().getThreadsUsed().set(5);
			player.getInventory().deleteItem(new Item(ItemNames.THREAD_1734, 1));
			player.getPackets().sendGameMessage("You use up one of your reeds of thread.");
		}
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			if (RandomUtility.random(30) == 5) {
				player.getInventory().deleteItem(new Item(ItemNames.NEEDLE_1733));
				player.getPackets().sendGameMessage("Your needle broke.");
				t.cancel();
			}
			handleThreadRemoval(player);
			amount--;
			if(amount <= 0)
				t.cancel();
		}
	}
	public static LeatherProduct getType(int comp) {
		return comp == 28 ? LeatherProduct.ARMOR : comp == 29 ? LeatherProduct.GLOVE : comp == 30 ? LeatherProduct.BOOT : comp == 31 ?
				LeatherProduct.VAMB : comp == 32 ? LeatherProduct.CHAPS : comp == 33 ? LeatherProduct.COIF : LeatherProduct.COWL;
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.CRAFTING_LEATHER);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(ItemNames.LEATHER_1741)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(data.item)});
	}
	
	@Override
	public int delay() {
		return 3;
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
		return true;
	}
	
	@AllArgsConstructor
	public enum LeatherProduct {
		ARMOR(28, 1129, 14, 25),
		GLOVE(29, 1059, 1, 13.8),
		BOOT(30, 1061, 7, 16.3),
		VAMB(31, 1063, 11, 22),
		CHAPS(32, 1095, 18, 27),
		COIF(33, 1169, 38, 37),
		COWL(34, 1167, 9, 18.5),
		;

		@SuppressWarnings("unused")
		private final int button;

		private final int item;
		
		private final int requirement;
		
		private final double xp;

		public static final ImmutableSet<LeatherProduct> VALUES = Sets.immutableEnumSet(EnumSet.allOf(LeatherProduct.class));
	}
}