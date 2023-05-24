package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

import io.vavr.control.Try;

@CommandSignature(alias = {"int"}, rights = {Rights.ADMINISTRATOR}, syntax = "Displays an interface")
public final class ShowInterfaceCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if (cmd.length < 2) {
			player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
			return;
		}
		Try.run(() -> player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]))).onFailure(failure -> player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId"));
	}
}