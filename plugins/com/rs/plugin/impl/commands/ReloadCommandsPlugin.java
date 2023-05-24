package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.CommandPluginDispatcher;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"reloadcommands"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Commands list")
public final class ReloadCommandsPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CommandPluginDispatcher.reload();
	}
}