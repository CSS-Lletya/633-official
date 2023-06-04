package skills.cooking;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class TeaCrafting {

	static int[] cupsEmpty = { 7728, 7732, 7735 };
	static int[] cupsFull = { 7730, 7733, 7736 };

	public static void execute(Player player, Item firstItem, Item secondItem) {
		if (firstItem.getDefinitions().getName().contains("Pot of tea"))
			for (int i = 0; i < 3; i++)
				if (secondItem.getId() == cupsEmpty[i]) {
					player.getInventory().deleteItem(firstItem.getId(), 1);
					player.getInventory().deleteItem(secondItem.getId(), 1);
					player.getInventory().addItem(cupsFull[i], 1);
					if (firstItem.getDefinitions().getName().contains("(1)"))
						player.getInventory().addItem(firstItem.getId() + 4, 1);
					else
						player.getInventory().addItem(firstItem.getId() + 2, 1);
					break;
				}
		int[][] combinations = {
				{ 7738, 7702, 7700 }, // Clay leaves
				{ 7738, 7714, 7712 }, // Porcelain leaves
				{ 7738, 7726, 7724 }, // Gilded leaves
				{ 7691, 7700, 7692, 7688 }, // Clay water
				{ 7691, 7712, 7704, 7688 }, // Porcelain water
				{ 7691, 7724, 7716, 7688 } // Gilded water
		};

		for (int[] combination : combinations) {
			if (firstItem.getId() == combination[0] && secondItem.getId() == combination[1]) {
				for (int i = 2; i < combination.length; i++) {
					player.getInventory().deleteItem(firstItem.getId(), 1);
					player.getInventory().deleteItem(secondItem.getId(), 1);
					player.getInventory().addItem(combination[i], 1);
				}
			}
		}
	}
}