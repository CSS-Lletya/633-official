package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"book"}, rights = {Rights.ADMINISTRATOR}, syntax = "Switches your magic book to parameter id")
public final class MagicBookSwitchCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getCombatDefinitions().setSpellBook(Integer.valueOf(cmd[1]));
    }
}