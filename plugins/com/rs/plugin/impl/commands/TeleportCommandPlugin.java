package com.rs.plugin.impl.commands;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"tele"}, rights = {Rights.ADMINISTRATOR}, syntax = "Teleports you to a specified location")
public final class TeleportCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if (cmd.length < 3) {
			player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
			return;
		}
		player.setNextWorldTile(new WorldTile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane()));
	}
}