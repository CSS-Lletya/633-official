package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"ri"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Iventory Items plugin list")
public final class ReloadInventoryItemsCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		InventoryPluginDispatcher.reload();
	}
}