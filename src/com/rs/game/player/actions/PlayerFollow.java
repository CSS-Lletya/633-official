package com.rs.game.player.actions;


import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.movement.route.RouteFinder;
import com.rs.game.movement.route.strategy.EntityStrategy;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

public class PlayerFollow extends Action {

	private Player target;
	
	public PlayerFollow(Optional<Entity> target) {
		this.target = target.get().toPlayer();
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player))
			return true;
		player.setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.isFinished() || target.isDead() || target.isFinished())
			return false;
		if (player.getPlane() != target.getPlane())
			return false;
		if (player.getMovement().isFrozen())
			return true;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = player.getSize();
		int maxDistance = 16;
		if (player.getPlane() != target.getPlane() || distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance || distanceY < -1 - maxDistance)
			return false;
		int lastFaceEntity = target.getLastFaceEntity();
		if (lastFaceEntity == player.getClientIndex() && target.getAction().getAction().get() instanceof PlayerFollow)
			player.addWalkSteps(target.getX(), target.getY());
		else if (!player.clipedProjectile(target, true) || !Utility.isOnRange(player.getX(), player.getY(), size,
				target.getX(), target.getY(), target.getSize(), 0)) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(),
					player.getPlane(), player.getSize(), new EntityStrategy(target), true);
			if (steps == -1)
				return false;

			if (steps > 0) {
				player.resetWalkSteps();

				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				for (int step = steps - 1; step >= 0; step--) {
					if (!player.addWalkSteps(bufferX[step], bufferY[step], 25, true))
						break;
				}
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(Player player) {
		player.setNextFaceEntity(null);
	}
}
