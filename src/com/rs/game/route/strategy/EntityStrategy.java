package com.rs.game.route.strategy;

import com.rs.game.Entity;
import com.rs.game.route.RouteStrategy;

public class EntityStrategy extends RouteStrategy {

    /**
     * Entity position x.
     */
    private int x;
    /**
     * Entity position y.
     */
    private int y;
    /**
     * Entity size.
     */
    private int size;
    /**
     * Access block flag, see RouteStrategy static final values.
     */
    private int accessBlockFlag;
    
    public EntityStrategy(Entity entity) {
	this(entity, 0);
    }
    
    public EntityStrategy(Entity entity, int accessBlockFlag) {
	this.x = entity.getX();
	this.y = entity.getY();
	this.size = entity.getSize();
	this.accessBlockFlag = accessBlockFlag;
    }

    @Override
    public boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY) {
	return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY, sizeXY, x - clipBaseX, y - clipBaseY, size, size, accessBlockFlag);
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
	return size;
    }

    @Override
    public int getApproxDestinationSizeY() {
	return size;
    }
    
    
    @Override
    public boolean equals(Object other) {
	if (!(other instanceof EntityStrategy))
	    return false;
	EntityStrategy strategy = (EntityStrategy)other;
	return x == strategy.x && y == strategy.y && size == strategy.size && accessBlockFlag == strategy.accessBlockFlag;
    }

}
