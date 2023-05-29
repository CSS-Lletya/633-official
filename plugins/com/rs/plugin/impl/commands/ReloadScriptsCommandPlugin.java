package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.system.scripts.ScriptManager;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"rs"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reloads the Custom Scipts list")
public final class ReloadScriptsCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		ScriptManager.load();
	}
}