package com.rs.plugin.impl.commands;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"update", "restart"}, rights = {Rights.ADMINISTRATOR}, syntax = "Start an System shutdown timer")
public final class UpdateServerCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	int delay = 120;
		if (cmd.length >= 2) {
			try {
				delay = Integer.valueOf(cmd[1]);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ;;restart secondsDelay(IntegerValue)");
				return;
			}
		}
		World.safeShutdown(true, (short) delay);
    }
}