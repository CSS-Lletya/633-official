package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"coord", "coords", "mypos", "pos"}, rights = {Rights.ADMINISTRATOR}, syntax = "Get your current coordinate")
public final class CoordinateCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getPackets().sendGameMessage("x: "+ player.getX() + " y: " + player.getY() + " h: " + player.getPlane());
	}
}