package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * This is just a dummy command to re-use for whatever testing needed.
 * 
 * @author Dennis
 *
 */
@CommandSignature(alias = { "test" }, rights = { Rights.PLAYER }, syntax = "Test a Command")
public class TestCommandPlugin implements CommandListener {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
//		player.getHintIconsManager().addHintIcon(3222, 3222, 0, 120, 0, 1, -1, false);
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE,
                "How many items would you like to make?<br>Choose a number, then click the item to begin.", 28, new int[] {1050}, null);
	}
}