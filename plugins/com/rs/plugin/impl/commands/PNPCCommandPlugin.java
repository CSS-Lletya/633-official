package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"pnpc", "benpc"}, rights = {Rights.ADMINISTRATOR}, syntax = "Turns player into an NPC, use -1 to reset")
public final class PNPCCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getAppearance().transformIntoNPC(Integer.parseInt(cmd[1]));
    }
}