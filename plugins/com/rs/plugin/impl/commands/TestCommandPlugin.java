package com.rs.plugin.impl.commands;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.content.InterfaceCreator;
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
//		player.getDialogueInterpreter().open(7888);
		new InterfaceCreator(player, 277).showRawIds(0, 11).writeString(3, "Neat title").hideComponent(6).sendItemToComponent(5, new Item(1050)).reset();
	}
}