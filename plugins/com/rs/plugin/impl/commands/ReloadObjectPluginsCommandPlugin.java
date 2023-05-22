package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.ObjectPluginDispatcher;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"relaodobjects", "ro"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Objects Plugins")
public final class ReloadObjectPluginsCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		ObjectPluginDispatcher.reload();
	}
}