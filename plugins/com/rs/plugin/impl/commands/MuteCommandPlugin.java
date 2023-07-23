package com.rs.plugin.impl.commands;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"mute"}, rights = {Rights.ADMINISTRATOR}, syntax = "Mutes a player")
public final class MuteCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	String name;
		Player target;
    	name = "";
		for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
		if (target == null)
			return;
        HostManager.add(target, HostListType.MUTED_IP);
        target.getDetails().getDaysMuted().set(3);
        target.getPackets().sendGameMessage("You have been muted for 3 days.");
        player.getPackets().sendGameMessage("You have muted " + target.getDisplayName() + " for 3 days.");
    }
}