package com.rs.plugin.impl.commands;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

import io.vavr.control.Try;

@CommandSignature(alias = {"item"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawn an Item")
public final class ItemCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if (cmd.length < 2) {
			player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
			return;
		}
		Try.run(() -> {
			int itemId = Integer.valueOf(cmd[1]);
			ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
			if (defs.isLended())
				return;
			if (defs.isOverSized()) {
				player.getPackets().sendGameMessage("The item appears to be oversized.");
				return;
			}
		}).onFailure(failure -> player.getPackets().sendGameMessage("Use: ::item id (optional:amount)"));
	}
}