package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;
import com.rs.utilities.Utility;

@CommandSignature(alias = {"intercomp", "ic", "debuginter"}, rights = {Rights.ADMINISTRATOR}, syntax = "Debugs interface components with ease")
public final class InterfaceComponentDebugCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	if (cmd.length < 2) {
    		player.getPackets().sendGameMessage("Use: ;;inter interfaceId");
			return;
		}
    	try {
			int interId = Integer.valueOf(cmd[1]);
			for (int componentId = 0; componentId < Utility
					.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
				player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
			}
		} catch (NumberFormatException e) {
			player.getPackets().sendGameMessage("Use: ;;inter interfaceId");
		}
    }
}