package com.rs.plugin.impl.inventory;

import java.util.Arrays;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@InventoryWrapper(itemId = {1783, 1937, 1921, 227, 6032, 6034, 4255, 4286, 1927, 1933}, itemNames = {  })
public class EmptyItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		System.out.println(option);
		if (option == 6) {
			Arrays.stream(EmptyData.values()).filter(toEmpty -> toEmpty.toEmpty == item.getId())
			.forEach(toEmpty -> {
				player.getInventory().getItems().set(slotId, new Item(toEmpty.getResult()));
                player.getInventory().refresh();
                player.getPackets().sendGameMessage("You empty out the " + ItemDefinitions.getItemDefinitions(item.getId()).getName() + ".");
                player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(toEmpty.toEmpty).getName() + "_Emptied").addStatistic("Items_Emptied");
			});
		}
	}
	
	@AllArgsConstructor
	enum EmptyData {
        BUCKET_OF_SAND(1783, 1925),
        JUG_OF_WATER(1937,3732),
        BOWL(1921, 1923),
        VIAL(227, 229),
        COMPOST(6032, 1925),
        SUPER_COMPOST(6034, 1925),
        BONEMEAL(4255, 1931),
        BUCKET_OF_SLIME(4286, 1925),
        BUCKET_OF_MILK(1927, 1925),
        POT_OF_FLOWER(1933, 1931),
        ;

		@Getter
        private int toEmpty, result;
    }
}