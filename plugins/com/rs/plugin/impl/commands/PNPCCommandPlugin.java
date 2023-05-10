package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"pnpc", "benpc"}, rights = {Rights.ADMINISTRATOR}, syntax = "Turns player into an NPC, use -1 to reset")
public final class PNPCCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getAppearance().transformIntoNPC(Integer.parseInt(cmd[1]));
    }
}