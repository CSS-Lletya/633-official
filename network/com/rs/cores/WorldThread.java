package com.rs.cores;

import java.util.Calendar;

import com.rs.Launcher;
import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.net.ServerChannelHandler;
import com.rs.utilities.RandomUtility;

import io.vavr.control.Try;
import lombok.Getter;

public class WorldThread implements Runnable {

	@Getter
	private static int ticks;

	private int pidSwapDelay = RandomUtility.inclusive(100, 150);

	@Override
	public void run() {
		Try.run(() -> {
			World.entities().forEach(Entity::processEntity);
			World.entities().forEach(Entity::processEntityUpdate);
			World.players().forEach(player -> {
				player.getPackets().sendLocalPlayersUpdate();
				player.getPackets().sendLocalNPCsUpdate();
			});
			World.entities().forEach(Entity::resetMasks);
			
			World.get().getTaskManager().sequence();
			if (--pidSwapDelay == 0) {
				World.shufflePids();
				pidSwapDelay = RandomUtility.random(100, 150);
			}
			if (ticks % 500 == 0)
				ServerChannelHandler.cleanMemory(ticks % 1000 == 0);
		ticks++;
		
		Calendar day = Calendar.getInstance();
		Launcher.getCalendar().setDay(day.get(Calendar.DAY_OF_WEEK));
		}).onFailure(Throwable::printStackTrace);
	}
}