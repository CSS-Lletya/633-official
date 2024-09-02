package com.rs.plugin.impl.commands;

import com.rs.game.dialogue.Mood;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
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
		player.dialogue(6970, d -> d.npc(Mood.happy, "I'm sorry but we aren't added into open633 yet.. Leave a message after the beeps? *BEEP*"));
//		player.dialogue(new DialogueEventListener(player, Entity.findNPC(2)) {
//			
//			@Override
//			public void start() {
//				npc(Mood.happy, "zzz");
//			}
//		});
//		player.dialogue(6970, d -> d.npc(Mood.happy, "zz"));
	}
}