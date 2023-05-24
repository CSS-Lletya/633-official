package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.NPCPluginDispatcher;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"rn", "reloadnpcs"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the NPC plugin")
public final class ReloadNPCSComandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		NPCPluginDispatcher.reload();
	}
}