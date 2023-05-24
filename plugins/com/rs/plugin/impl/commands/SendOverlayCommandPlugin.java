package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"overlay", "sendoverlay"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens an overlay (walkable interface)")
public final class SendOverlayCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getInterfaceManager().sendOverlay(Integer.valueOf(cmd[1]));
    }
}