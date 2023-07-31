package com.rs.plugin.impl.objects.region;

import java.util.Arrays;

import com.rs.game.dialogue.impl.StairsLaddersDialogue;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {1738,1740, 1747, 1754, 1746, 1757, 36695}, name = {})
public class HerosGuildRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, 1738, "climb-up", () -> player.setNextWorldTile(new WorldTile(2897, 3513, 1)));
		object.doAction(optionId, 1740, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 1747, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 1746, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 1754, "climb-down", () -> player.setNextWorldTile(new WorldTile(2893, 9907, 0)));
		object.doAction(optionId, 1757, "climb-up", () -> player.setNextWorldTile(new WorldTile(2893, 3507, 0)));
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		for (Item toChageItem : player.getInventory().getItems().getItems()) {
            if (toChageItem == null)
                continue;
            int[][] RECHARGABLES = {{1704, 1712}, {1706, 1712}, {1708, 1712}, {1710, 1712}, // glory
                    {2572, 20659}, {20653, 20659}, {20655, 20659}, {20657, 20659}, // wealth
                    {11113, 11105}, {11111, 11105}, {11109, 11105}, {11107, 11105} // skills necklace
            };
            Arrays.stream(RECHARGABLES).filter(jewelry -> toChageItem.getId() == jewelry[0])
            .forEach(rechargable -> {
            	player.getInventory().replaceItems(new Item(rechargable[0]), new Item(rechargable[1]));
            	player.dialogue(d -> d.item(toChageItem.getId(), "You dip the " + toChageItem.getName() + " in the fountain..."));
            });
        }
	}
}