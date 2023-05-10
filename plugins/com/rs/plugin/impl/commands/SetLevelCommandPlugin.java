package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

import skills.Skills;

@CommandSignature(alias = {"setlevel", "setlvl"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sets target skill to target level")
public final class SetLevelCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	if (cmd.length < 3) {
    		player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
			return;
		}
		try {
			int skill1 = Integer.parseInt(cmd[1]);
			int level1 = Integer.parseInt(cmd[2]);
			if (level1 < 0 || level1 > 99) {
				player.getPackets().sendGameMessage("Please choose a valid level.");
				return;
			}
			if (skill1 < 0 || skill1 > 26) {
				player.getPackets().sendGameMessage("Please choose a valid skill.");
				return;
			}
			player.getSkills().set(skill1, level1);
			player.getSkills().setXp(skill1, Skills.getXPForLevel(level1));
			player.getAppearance().generateAppearenceData();
		} catch (NumberFormatException e) {
			player.getPackets().sendGameMessage("Usage ;;setlevel/;;setlvl skillId level");
		}
    }
}