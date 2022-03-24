package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"Bank"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens Bank")
public final class BankCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        player.getBank().openBank();
    }
}