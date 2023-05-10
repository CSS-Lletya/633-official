package com.rs.plugin.impl.commands;

import java.util.stream.IntStream;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

import skills.Skills;

/**
 * This is just a dummy command to re-use
 * for whatever testing needed.
 * @author Dennis
 *
 */
@CommandSignature(alias = {"reset", "rs", "resetskills"}, rights = {Rights.PLAYER}, syntax = "Resets all your skills back to level 1")
public final class ResetStatsCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		IntStream.rangeClosed(0, 23).forEach(skill -> {
			 player.getSkills().set(skill, 1);
			 player.getSkills().setXp(skill, Skills.getXPForLevel(1));
		});
        player.getSkills().set(24, 1);
        player.getSkills().setXp(24, Skills.getXPForLevel(1));
        player.getAppearance().generateAppearenceData();
	}
}