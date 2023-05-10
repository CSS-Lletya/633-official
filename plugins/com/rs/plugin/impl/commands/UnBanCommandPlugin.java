package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"unban"}, rights = {Rights.ADMINISTRATOR}, syntax = "Unbans a player")
public final class UnBanCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        HostManager.remove(player.getDisplayName(), HostListType.BANNED_IP, true);
    }
}