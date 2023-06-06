package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"reloadrsi"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the RS Interface list")
public final class ReloadRSInterfacesCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		RSInterfacePluginDispatcher.reload();
	}
}