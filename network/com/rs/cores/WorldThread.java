package com.rs.cores;

import java.util.Calendar;

import com.rs.Launcher;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.net.ServerChannelHandler;
import com.rs.utilities.RandomUtility;

import io.vavr.control.Try;

public class WorldThread implements Runnable {

	public static int lastCycle;

	private int pidSwapDelay = RandomUtility.inclusive(100, 150);

	@Override
	public final void run() {
		
		Try.run(() -> {
			World.players().forEach(Player::processEntity);
			World.npcs().forEach(NPC::processEntity);

			World.players().forEach(Player::processEntityUpdate);
			World.npcs().forEach(NPC::processEntityUpdate);

			World.players().forEach(player -> {
				player.getPackets().sendLocalPlayersUpdate();
				player.getPackets().sendLocalNPCsUpdate();
			});

			World.players().forEach(Player::resetMasks);
			World.npcs().forEach(NPC::resetMasks);

			World.get().getTaskManager().sequence();

			if (--pidSwapDelay == 0) {
				World.shufflePids();
				pidSwapDelay = RandomUtility.random(100, 150);
			}
			if (lastCycle % 500 == 0)
				ServerChannelHandler.cleanMemory(lastCycle % 1000 == 0);
		lastCycle++;
		
		Calendar day = Calendar.getInstance();
		Launcher.getCalendar().setDay(day.get(Calendar.DAY_OF_WEEK));
		}).onFailure(Throwable::printStackTrace);
	}
}