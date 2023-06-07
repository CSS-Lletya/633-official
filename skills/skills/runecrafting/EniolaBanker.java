package skills.runecrafting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class EniolaBanker {
	
	public static boolean sendInterfaceFunctionality(Player player, int componentId) {
	    int[] runeIds = {556, 558, 555, 557, 554, 559, 564, 562, 9075, 563, 560, 565, 561, 566};
	    if (componentId >= 28 && componentId <= 41) {
	        int index = componentId - 28;
	        checkRunesBankOrCollect(player, runeIds[index]);
	        return true;
	    }
	    return false;
	}

	private static void checkRunesBankOrCollect(Player player, int runeId) {
		Item runes = new Item(runeId, 20);
		if (player.getInventory().containsItem(runes)) {
			player.getInventory().removeItems(runes);
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(runes.getId()).getName()+"_Traded").addStatistic("Eniola_Runes_Traded");
			player.getBank().openBank();
		} else {
			player.getPackets().sendGameMessage("You don't have enough " + runes.getName() + "s!");
			player.getInterfaceManager().closeInterfaces();
		}
	}
}