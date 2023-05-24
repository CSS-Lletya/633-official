package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"svb", "varbit", "sendvarbit"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sends an Var Bit")
public final class SendVarBitCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getVarsManager().sendVarBit(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
    }
}