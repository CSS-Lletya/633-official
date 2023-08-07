package skills.crafting;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;
import skills.Skills;

public class Tanning {

	@AllArgsConstructor
	public enum TanningProduct {
		SOFT_LEATHER(0, 1739, 1741, 1, 1),
		HARD_LEATHER(1, 1739, 1743,3,28),
		SNAKESKIN(2, 6287, 6289,15,45),
		SNAKESKIN2(3, 6287, 6289,20,45),
		GREEN_DHIDE(4, 1753, 1745,20,57),
		BLUEDHIDE(5, 1751, 2505,20, 66),
		REDDHIDE(6, 1749, 2507,20, 73),
		BLACKDHIDE(7, 1747, 2509,20,79);

		/**
		 * The button.
		 */
		private final int button;

		/**
		 * The item needed.
		 */
		private final int item;

		/**
		 * The product.
		 */
		private final int product;
		
		private final int price;
		
		private final int craftingLevel;

		public static final ImmutableSet<TanningProduct> VALUES = Sets.immutableEnumSet(EnumSet.allOf(TanningProduct.class));
	}
	
	public static int requestedAmount;
	
	private static int getAmount(int packetId) {
		return packetId == 11 ? 1 : packetId == 29 ? 5 : packetId == 31 ? 10 : packetId == 32 ? Integer.MAX_VALUE : 0;
	}
	
	public static void handleTanning(Player player, int componentId, int packetId, int inputAmount) {
		TanningProduct.VALUES.stream().filter(comp -> comp.button == componentId)
		.forEach(leather -> {
			int tannableCount = player.getInventory().getAmountOf(leather.item);
			if (player.getSkills().getTrueLevel(Skills.CRAFTING) < leather.craftingLevel) {
				player.dialogue(2824, d -> d.npc(Mood.sad,"Young traveler! You aren't skilled enough to make this leather yet. Come back when you have a Crafting level of at least "
                        + leather.craftingLevel + "."));
				player.getPackets().sendGameMessage("You need a crafting level of at least " + leather.craftingLevel + "to tan this hide.");
				return;
			}
			if (player.getInventory().canPay(leather.price)) {
				if (tannableCount == 0) {
					player.dialogue(2824, d -> d.npc(Mood.sad, "Ahhh... novice mistake, you must bring me at least one " 
		                    + ItemDefinitions.getItemDefinitions(leather.item).getName()
		                    .toLowerCase()
		                    + " in order to tan " + ItemDefinitions
		                    .getItemDefinitions(leather.product).getName().toLowerCase()
		                    + "."));
					return;
				}
				requestedAmount = inputAmount == 0 ? getAmount(packetId) : inputAmount;

				int maximumQuantity = leather.price == 0 ? requestedAmount : player.getInventory().getNumberOf(995) / leather.price;
				if (maximumQuantity > 0) {
					if (requestedAmount > maximumQuantity)
						requestedAmount = maximumQuantity;
					if (requestedAmount > tannableCount)
						requestedAmount = tannableCount;
				}
				player.getInterfaceManager().closeInterfaces();
				player.getInventory().deleteItem(new Item(leather.item, requestedAmount));
				player.getInventory().addItem(new Item(leather.product, requestedAmount));
			} else
				player.dialogue(2824, d -> d.npc(Mood.sad, "Oh no, it looks like you've ran out of coins. Come back later once you have " + leather.price + " coins."));
		});
	}
}
