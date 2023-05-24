package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"ban"}, rights = {Rights.ADMINISTRATOR}, syntax = "Bans a player")
public final class BanCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        HostManager.add(player, HostListType.BANNED_IP, true);
    }
}