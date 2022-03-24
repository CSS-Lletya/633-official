package com.rs.game.player.actions;


import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.game.route.RouteFinder;
import com.rs.game.route.strategy.EntityStrategy;
import com.rs.utilities.Utility;

public class PlayerFollow extends Action {

	private Player target;
	
	public PlayerFollow(Player player, Optional<Entity> target) {
		super(player, target);
		this.target = target.get().toPlayer();
	}

	@Override
	public boolean start() {
		getPlayer().setNextFaceEntity(target);
		if (checkAll())
			return true;
		getPlayer().setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll() {
		if (getPlayer().isDead() || getPlayer().isFinished() || target.isDead() || target.isFinished())
			return false;
		if (getPlayer().getPlane() != target.getPlane())
			return false;
		if (getPlayer().getMovement().isFrozen())
			return true;
		int distanceX = getPlayer().getX() - target.getX();
		int distanceY = getPlayer().getY() - target.getY();
		int size = getPlayer().getSize();
		int maxDistance = 16;
		if (getPlayer().getPlane() != target.getPlane() || distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance || distanceY < -1 - maxDistance)
			return false;
		int lastFaceEntity = target.getLastFaceEntity();
		if (lastFaceEntity == getPlayer().getClientIndex() && target.getAction().getAction().get() instanceof PlayerFollow)
			getPlayer().addWalkSteps(target.getX(), target.getY());
		else if (!getPlayer().clipedProjectile(target, true) || !Utility.isOnRange(getPlayer().getX(), getPlayer().getY(), size,
				target.getX(), target.getY(), target.getSize(), 0)) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, getPlayer().getX(), getPlayer().getY(),
					getPlayer().getPlane(), getPlayer().getSize(), new EntityStrategy(target), true);
			if (steps == -1)
				return false;

			if (steps > 0) {
				getPlayer().resetWalkSteps();

				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				for (int step = steps - 1; step >= 0; step--) {
					if (!getPlayer().addWalkSteps(bufferX[step], bufferY[step], 25, true))
						break;
				}
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean process() {
		return checkAll();
	}

	@Override
	public int processWithDelay() {
		return 0;
	}

	@Override
	public void stop() {
		getPlayer().setNextFaceEntity(null);
	}
}
