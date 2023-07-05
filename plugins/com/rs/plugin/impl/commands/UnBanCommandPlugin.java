package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"unban"}, rights = {Rights.ADMINISTRATOR}, syntax = "Unbans a player")
public final class UnBanCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        HostManager.remove(player.getDisplayName(), HostListType.BANNED_IP);
    }
}