package com.rs.plugin.impl.commands;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = { "getip" }, rights = { Rights.ADMINISTRATOR }, syntax = "Get a target players IP")
public final class GetIPCommandPlugin implements Command {
	@Override
	public void execute(Player player, String[] cmd, String command) {
		String name;
		name = "";
		for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		Player targetPlayer = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
		if (targetPlayer == null) {
			player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
		} else if (GameConstants.STAFF.keySet().stream().anyMatch(staff -> targetPlayer.getUsername().equalsIgnoreCase(staff))) {
				player.getPackets().sendGameMessage("Silly kid, you can't check a developers IP address!");
				return;
		}
		player.getPackets().sendGameMessage(targetPlayer.getDisplayName() + "'s IP is " + targetPlayer.getSession().getIP() + ".");
	}
}