package com.rs.plugin.impl.commands;

import java.util.stream.IntStream;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

import skills.Skills;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"max", "master"}, rights = {Rights.PLAYER}, syntax = "Unlock all skills to 99")
public final class MasterCommandPlugin implements CommandListener {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		IntStream.rangeClosed(0, 23).forEach(skill -> {
			 player.getSkills().set(skill, 99);
			 player.getSkills().setXp(skill, Skills.getXPForLevel(99));
		});
        player.getSkills().set(24, 120);
        player.getSkills().setXp(24, Skills.getXPForLevel(120));
        player.getAppearance().generateAppearenceData();
	}
}