package com.rs.cores;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.net.ServerChannelHandler;
import com.rs.utilities.Utility;

import io.vavr.control.Try;

public final class WorldThread extends Thread {

	public static final long WORLD_CYCLE = 0;

	public WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Utility.currentTimeMillis();
			Try.run(() -> {

				World.get().getTaskManager().sequence();

				World.players().forEach(player -> player.processEntity());
				World.npcs().forEach(npc -> npc.processEntity());

				World.players().forEach(player -> player.processEntityUpdate());
				World.npcs().forEach(npc -> npc.processEntityUpdate());

				World.players().forEach(player -> {
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				});

				World.players().forEach(player -> player.resetMasks());
				World.npcs().forEach(npc -> npc.resetMasks());

				ServerChannelHandler.processSessionQueue();
			}).onFailure(fail -> fail.printStackTrace());
			LAST_CYCLE_CTM = Utility.currentTimeMillis();
			long sleepTime = GameConstants.WORLD_CYCLE_MS + currentTime - LAST_CYCLE_CTM;
			if (sleepTime <= 0)
				continue;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static long LAST_CYCLE_CTM;

}
