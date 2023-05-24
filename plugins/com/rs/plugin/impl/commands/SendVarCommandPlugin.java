package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"sv", "var", "sendvar"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sends an interface Config/Var")
public final class SendVarCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getVarsManager().sendVar(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
    }
}