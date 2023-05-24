package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"Bank"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens Bank")
public final class BankCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
        player.getBank().openBank();
    }
}