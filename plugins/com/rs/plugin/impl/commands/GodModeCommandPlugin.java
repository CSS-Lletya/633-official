package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"g", "god"}, rights = {Rights.ADMINISTRATOR}, syntax = "Be like Homelander, without the dark side")
public final class GodModeCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.setHitpoints(5000000);
    	player.getPrayer().setPoints(5000000);
    	for (int i = 0; i < 10; i++)
			player.getCombatDefinitions().getBonuses()[i] = 50000;
		for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
			player.getCombatDefinitions().getBonuses()[i] = 50000;
    }
}