package com.rs.plugin.impl.commands;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;
import com.rs.utilities.Utility;

@CommandSignature(alias = {"kick"}, rights = {Rights.ADMINISTRATOR}, syntax = "Kicks a target player off their session")
public final class KickPlayerCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        String name;
        Player target;
        
        name = "";
		for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		target = World.getPlayerByDisplayName(name);
		if (target == null) {
			player.getPackets().sendGameMessage(Utility.formatPlayerNameForDisplay(name) + " is not logged in.");
			return;
		}
		target.getSession().forceLogout(target);
		player.getPackets().sendGameMessage("You have force kicked: " + target.getDisplayName() + ".");
    }
}