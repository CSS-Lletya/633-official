package com.rs.cores;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Utility;

import io.vavr.control.Try;

public final class WorldThread implements Runnable {

	public static long LAST_CYCLE_CTM;
	
	private static int pidSwapDelay = RandomUtility.inclusive(100, 150);
	
	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Utility.currentTimeMillis();
			Try.run(() -> {
				
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
				
				if (--pidSwapDelay == 0) {
                	World.shufflePids();
                    pidSwapDelay = RandomUtility.random(100, 150);
                }
				World.get().getTaskManager().sequence();
			}).onFailure(Throwable::printStackTrace);
			LAST_CYCLE_CTM = Utility.currentTimeMillis();
			long sleepTime = GameConstants.WORLD_CYCLE_MS + currentTime - LAST_CYCLE_CTM;
			if (sleepTime <= 0)
				continue;
			Try.run(() -> Thread.sleep(sleepTime)).onFailure(f -> f.printStackTrace());
		}
	}
}