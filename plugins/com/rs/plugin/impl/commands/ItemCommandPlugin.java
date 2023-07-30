package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

import io.vavr.control.Try;

@CommandSignature(alias = {"item"}, rights = {Rights.ADMINISTRATOR}, canIgnoreCondition = true, syntax = "Spawn an Item")
public final class ItemCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Try.run(() -> {
			if (cmd.length == 3) {
				player.getInventory().addItem(Integer.parseInt(cmd[1]),
						Integer.parseInt(cmd[2]));
			} else {
				player.getInventory().addItem(Integer.parseInt(cmd[1]), 1);
			}
			player.getInventory().refresh();
		}).onFailure(failure -> player.getPackets().sendGameMessage("Use: ::item id (optional:amount)"));
	}
}