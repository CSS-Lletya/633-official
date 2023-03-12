package com.rs.plugin.impl.commands;

import com.rs.content.mapzone.impl.TestMapZone;
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
//		player.getPackets().sendSpawnedObject(new GameObject(2, 10, 0, player));
		player.getMapZoneManager().submitMapZone(player, new TestMapZone());
		System.out.println(player.getMapZoneManager().getMapZone(player).isPresent());
	}
}