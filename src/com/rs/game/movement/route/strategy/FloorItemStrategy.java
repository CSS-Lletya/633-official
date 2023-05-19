package com.rs.game.movement.route.strategy;

import com.rs.game.item.FloorItem;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteStrategy;

public class FloorItemStrategy extends RouteStrategy {

	/**
	 * Entity position x.
	 */
	private int x;
	/**
	 * Entity position y.
	 */
	private int y;

	public FloorItemStrategy(FloorItem entity) {
		this.x = entity.getTile().getX();
		this.y = entity.getTile().getY();
	}

	public FloorItemStrategy(WorldTile entity, boolean junk) {
		this.x = entity.getX();
		this.y = entity.getY();
	}

	@Override
	public boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY) {
		return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY,
				sizeXY, x - clipBaseX, y - clipBaseY, 1, 1, 0);
	}

	@Override
	public int getApproxDestinationX() {
		return x;
	}

	@Override
	public int getApproxDestinationY() {
		return y;
	}

	@Override
	public int getApproxDestinationSizeX() {
		return 1;
	}

	@Override
	public int getApproxDestinationSizeY() {
		return 1;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof FloorItemStrategy))
			return false;
		FloorItemStrategy strategy = (FloorItemStrategy) other;
		return x == strategy.x && y == strategy.y;
	}

}
