package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use for whatever testing needed.
 * 
 * @author Dennis
 *
 */
@CommandSignature(alias = { "test" }, rights = { Rights.PLAYER }, syntax = "Test a Command")
public class TestCommandPlugin implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
//		player.getHintIconsManager().addHintIcon(3222, 3222, 0, 120, 0, 1, -1, false);
	}
}