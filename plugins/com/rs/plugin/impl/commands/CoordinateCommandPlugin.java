package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"coord", "coords", "mypos", "pos"}, rights = {Rights.ADMINISTRATOR}, syntax = "Get your current coordinate")
public final class CoordinateCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		final String msg = "x: "+ player.getX() + " y: " + player.getY() + " h: " + player.getPlane()
				+ " region: " + player.getRegionId() + " cx: " + player.getChunkX() + " cy: " + player.getChunkY();
		player.getPackets().sendGameMessage(msg);
		System.out.println(msg);
	}
}