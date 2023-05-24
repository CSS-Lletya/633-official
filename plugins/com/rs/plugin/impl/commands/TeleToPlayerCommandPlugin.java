package com.rs.plugin.impl.commands;

import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"teleto", "top", "toplayer"}, rights = {Rights.ADMINISTRATOR}, syntax = "Teleports you to a target player")
public final class TeleToPlayerCommandPlugin implements CommandListener {
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
		player.setNextWorldTile(new WorldTile(target));
    }
}