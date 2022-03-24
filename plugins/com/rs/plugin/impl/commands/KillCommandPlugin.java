package com.rs.plugin.impl.commands;

import com.rs.game.map.World;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.Hit.HitLook;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"kill"}, rights = {Rights.ADMINISTRATOR}, syntax = "Kill a specified Player")
public final class KillCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		String name;
		Player target;
		name = "";
		for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		target = World.getPlayerByDisplayName(name);
		if (target == null)
			return;
		target.applyHit(new Hit(target, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
		target.getMovement().stopAll();
	}
}