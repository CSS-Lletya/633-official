package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.runecrafting.PouchType;
import skills.runecrafting.Runecrafting;

@InventoryWrapper(itemId = {5509, 5510, 5512, 5514})
public class RunecraftingPouchesItemPlugin implements InventoryListener {

	@Override
	public void execute(Player player, Item item, int option) throws Exception {
		switch(item.getId()) {
		case 5509:
			if (option == 1)
				Runecrafting.fill(player, PouchType.SMALL);
			else if (option == 2)
				Runecrafting.empty(player,  PouchType.SMALL);
			else if (option == 3)
				Runecrafting.examine(player,  PouchType.SMALL);
			break;
		case 5510:
			if (option == 1)
				Runecrafting.fill(player, PouchType.MEDIUM);
			else if (option == 2)
				Runecrafting.empty(player,  PouchType.MEDIUM);
			else if (option == 3)
				Runecrafting.examine(player,  PouchType.MEDIUM);
			break;
		case 5512:
			if (option == 1)
				Runecrafting.fill(player, PouchType.LARGE);
			else if (option == 2)
				Runecrafting.empty(player,  PouchType.LARGE);
			else if (option == 3)
				Runecrafting.examine(player,  PouchType.LARGE);
			break;
		case 5514:
			if (option == 1)
				Runecrafting.fill(player, PouchType.GIANT);
			else if (option == 2)
				Runecrafting.empty(player,  PouchType.GIANT);
			else if (option == 3)
				Runecrafting.examine(player,  PouchType.GIANT);
			break;
		}
	}
}