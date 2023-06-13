package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.CommandPluginDispatcher;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"rc", "reloadcommands"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Commands plugin")
public final class ReloadCommandsCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CommandPluginDispatcher.reload();
	}
}