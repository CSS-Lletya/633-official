package skills;

import com.rs.game.item.Item;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SkillcapeMasters {
	Attack(9747, 9748, 9749, "a master in the fine art of attacking"),
	Defence(9753, 9754, 9755, "a master in the fine art of defence"),
	Strength(9750, 9751, 9752,  "as strong as is possible"),
	Constitution(9768, 9769, 9770, "as resilient as is possible"),
	Ranging(9756, 9757, 9758,  "a master in the fine art of ranging"),
	Prayer(9759, 9760, 9761,"as devoted to prayer as possible"),
	Magic(9762, 9763, 9764, "as powerful a wizard as I"),
	Cooking(9801, 9802, 9803, "a master in the culinary arts"),
	Woodcutting(9807, 9808, 9809, "a master woodsman"),
	Fletching(9783, 9784, 9785, "a master in the fine art of fletching"),
	Fishing(9798, 9799, 9800, "a master fisherman"),
	Firemaking(9804, 9805, 9806, "a master of fire"),
	Crafting(9780, 9781, 9782, "a master craftsman"),
	Smithing(9795, 9796, 9797,  "a master blacksmith"),
	Mining(9792, 9793, 9794, "a master miner"),
	Herblore(9774, 9775, 9776, "a master herbalist"),
	Agility(9771, 9772, 9773, "as agile as possible"),
	Thieving(9777, 9778, 9779, "a master thief"),
	Slayer(9786, 9787, 9788, "an incredible student"),
	Farming(9810, 9811, 9812, "a master farmer"),
	Runecrafting(9765, 9766, 9767, "a master runecrafter"),
	Hunter(9948, 9949, 9950, "a master hunter"),
	Construction(9789, 9790, 9791, "a master home builder"),
	Summoning(12169, 12170, 12171, "a master summoner"),
	Dungeoneering(18508, 18509, 18510, "a master dungeon delver");

	public final int untrimmed, trimmed, hood;
	public final String verb; 
	
	public void startTransaction(Player player) {
		if (player.getInventory().getFreeSlots() < 2) {
			player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
			return;
		}
		if (!player.getInventory().canPay(99_000)) {
			player.getPackets().sendGameMessage("You cannot afford to purchase this skillcape.");
		} else {
			player.getInventory().addItem(player.getSkills().checkMulti99s() ? trimmed : untrimmed, 1);
			player.getInventory().addItem(new Item(hood));
		}
	}
}