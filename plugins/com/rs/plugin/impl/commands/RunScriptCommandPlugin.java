package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"script", "runscript"}, rights = {Rights.ADMINISTRATOR}, syntax = "Run a Script quickly")
public final class RunScriptCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getPackets().sendRunScript(Integer.valueOf(cmd[1]));
    }
}