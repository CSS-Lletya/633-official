package com.rs.plugin.impl.commands;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = { "rtele", "rt" }, rights = { Rights.ADMINISTRATOR }, syntax = "Teleport to a Region via ID")
public final class RegionTeleportCommandPlugin implements CommandListener {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		int x = (((id >> 8) & 0xff) << 6) + 32;
		int y = (((id) & 0xff) << 6) + 32;
		player.setNextWorldTile(new WorldTile(x, y, 0));
	}
}