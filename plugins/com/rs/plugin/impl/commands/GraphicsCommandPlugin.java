package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.encoders.other.Graphics;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"gfx", "graphic", "graphics"}, rights = {Rights.ADMINISTRATOR}, syntax = "Perform an graphic")
public final class GraphicsCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1])));
	}
}