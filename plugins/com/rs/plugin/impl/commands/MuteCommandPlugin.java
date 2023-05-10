package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"mute"}, rights = {Rights.ADMINISTRATOR}, syntax = "Mutes a player")
public final class MuteCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        HostManager.add(player, HostListType.MUTED_IP, true);
    }
}