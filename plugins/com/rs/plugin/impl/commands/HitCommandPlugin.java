package com.rs.plugin.impl.commands;

import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"hit", "smack"}, rights = {Rights.ADMINISTRATOR}, syntax = "Perform an graphic")
public final class HitCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.applyHit(new Hit(player, Integer.valueOf(cmd[1]), HitLook.MELEE_DAMAGE));
	}
}