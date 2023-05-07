package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.network.sql.PassiveDatabaseWorker;
import com.rs.network.sql.queries.TestSQLPlugin;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

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
		LogUtility.log(LogType.SQL, "hey lol");
		player.getVarsManager().sendVar(1496, 0);
//		player.applyHit(new Hit(player, 5, HitLook.POISON_DAMAGE));
//		player.getVarsManager().sendVar(465, -1);
//		player.getVarsManager().sendVar(802, -1).submitVarToMap(802, -1);
//		player.getVarsManager().sendVar(1085, 12);// Zombie Hand enable
//		player.getVarsManager().sendVar(2032, 7341);// Seal of approval
//		player.getVarsManager().sendVar(2033, 1043648799);// Seal of approval req?
//		player.getVarsManager().sendVar(1921, -893736236);// Puppet master emote
//		player.getVarsManager().sendVar(1404, 123728213);
//		player.getVarsManager().sendVar(1405, 1720);
//		player.getVarsManager().sendVar(1407, 5370);
//		player.getVarsManager().sendVar(1160, -1);
//		player.getVarsManager().sendVar(1583, 511305630);
//		player.getVarsManager().sendVar(1597, -1);
//		player.getVarsManager().sendVar(1842, -1);
//		player.getVarsManager().sendVar(1958, 534);
//		player.getVarsManager().sendVar(313, -1);// Emotes
//		player.getVarsManager().sendVar(2432, -1);
//		player.getVarsManager().sendVar(1958, 534);
//		player.getVarsManager().sendVar(2405, -1);
//		player.getVarsManager().sendVar(2458, -1);
//		player.getVarsManager().varMap.forEach((k, v) -> player.getVarsManager().sendVar(k, v));
	}
}