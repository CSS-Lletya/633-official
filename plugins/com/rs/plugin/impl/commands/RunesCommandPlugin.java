package com.rs.plugin.impl.commands;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"runes"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawn all the Runes for Magic spells")
public final class RunesCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		for (int i = 554; i < 565; i++) {
			player.getInventory().addItem(new Item(i, Integer.MAX_VALUE));
		}
	}
}