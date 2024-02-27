package com.rs.plugin.impl.commands;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.content.InterfaceCreator;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use for whatever testing needed.
 * 
 * @author Dennis
 *
 */
@CommandSignature(alias = { "test" }, rights = { Rights.PLAYER }, syntax = "Test a Command")
public class TestCommandPlugin implements CommandListener {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		ItemsContainer<Item> bank = new ItemsContainer<Item>(28);
		bank.add(new Item(1050));
	    new InterfaceCreator(player, 468, bank)
	    .sendInterSetItemsOptionsScript(2, 2, 8, 6, "Take", "Examine")
	    .sendUnlockIComponentOptionSlots(2, 0, 48, 0, 1, 2)
	    .refreshItemContainer(2)
	    .renderAsInterface();
	}
}