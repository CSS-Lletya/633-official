package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"hide"}, rights = {Rights.ADMINISTRATOR}, syntax = "Hide for players POV")
public final class HideCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getAppearance().switchHidden();
		player.getPackets().sendGameMessage("Am i hidden? " + player.getAppearance().isHidePlayer());
    }
}