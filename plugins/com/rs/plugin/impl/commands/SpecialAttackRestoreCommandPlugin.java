package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"spec"}, rights = {Rights.ADMINISTRATOR}, syntax = "Restores your special attack to 100%")
public final class SpecialAttackRestoreCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.getCombatDefinitions().resetSpecialAttack();
    }
}