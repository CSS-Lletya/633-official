package com.rs.game.player.queue.impl;

import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteFinder;
import com.rs.game.movement.route.strategy.FixedTileStrategy;
import com.rs.game.player.queue.PlayerScript;
import com.rs.game.player.queue.RSQueueType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WalkScript extends PlayerScript {


	private final int x,y;
	private final boolean forceRun;
	
	@Override
	public void process() {
		player.getMovement().stopAll();
		if (forceRun)
			player.setRun(forceRun);
		
		if (player.getInterfaceManager().containsScreenInter())
			player.getInterfaceManager().closeInterfaces();

		player.getSkillAction().ifPresent(skill -> skill.cancel());

		int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(),
				player.getSize(), new FixedTileStrategy(x, y), true);
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		int last = -1;
		for (int i = steps - 1; i >= 0; i--) {
			if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true))
				break;
			last = i;
		}

		if (last != -1) {
			WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
			player.getPackets().sendMinimapFlag(
					tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()),
					tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		} else {
			player.getPackets().sendResetMinimapFlag();
		}
	}

	@Override
	public RSQueueType type() {
		return RSQueueType.Strong;
	}
}