package com.rs.cores;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import io.vavr.control.Try;

public final class WorldThread extends Thread {

	public WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	public static long LAST_CYCLE_CTM;
	
	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Utility.currentTimeMillis();
			Try.run(() -> {
				
				World.get().getTaskManager().sequence();
				World.players().forEach(player -> player.processEntity());
				World.npcs().forEach(npc -> npc.processEntity());

				World.players().forEach(Player::processEntityUpdate);
				World.npcs().forEach(NPC::processEntityUpdate);

				World.players().forEach(player -> {
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				});
				
				World.players().forEach(Player::resetMasks);
				World.npcs().forEach(NPC::resetMasks);
			}).onFailure(Throwable::printStackTrace);
			LAST_CYCLE_CTM = Utility.currentTimeMillis();
			long sleepTime = GameConstants.WORLD_CYCLE_MS + currentTime - LAST_CYCLE_CTM;
			if (sleepTime <= 0)
				continue;
			Try.run(() -> Thread.sleep(sleepTime)).onFailure(f -> f.printStackTrace());
		}
	}
}