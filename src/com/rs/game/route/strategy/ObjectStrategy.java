package com.rs.game.route.strategy;

import com.rs.game.map.GameObject;
import com.rs.game.route.RouteStrategy;

public class ObjectStrategy extends RouteStrategy {

	/**
	 * Contains object pos x.
	 */
	private int x;
	/**
	 * Contains object pos y.
	 */
	private int y;
	/**
	 * Contains object route type.
	 */
	private int routeType;
	/**
	 * Contains object type.
	 */
	private int type;
	/**
	 * Contains object rotation.
	 */
	private int rotation;
	/**
	 * Contains object size X.
	 */
	private int sizeX;
	/**
	 * Contains object size Y.
	 */
	private int sizeY;
	/**
	 * Contains block flag.
	 */
	private int accessBlockFlag;

	public ObjectStrategy(GameObject object) {
		this.x = object.getX();
		this.y = object.getY();
		this.routeType = getType(object);
		this.type = object.getType();
		this.rotation = object.getRotation();
		this.sizeX = rotation == 0 || rotation == 2 ? object.getDefinitions().getSizeX()
				: object.getDefinitions().getSizeY();
		this.sizeY = rotation == 0 || rotation == 2 ? object.getDefinitions().getSizeY()
				: object.getDefinitions().getSizeX();
		this.accessBlockFlag = object.getDefinitions().getCflag();
		if (rotation != 0)
			accessBlockFlag = ((accessBlockFlag << rotation) & 0xF) + (accessBlockFlag >> (4 - rotation));
	}

	@Override
	public boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY) {
		switch (routeType) {
		case 0:
			return RouteStrategy.checkWallInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY,
					x - clipBaseX, y - clipBaseY, type, rotation);
		case 1:
			return RouteStrategy.checkWallDecorationInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY,
					x - clipBaseX, y - clipBaseY, type, rotation);
		case 2:
			return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY,
					sizeXY, sizeXY, x - clipBaseX, y - clipBaseY, sizeX, sizeY, accessBlockFlag);
		case 3:
			return currentX == x && currentY == y;
		}
		return false;
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
		return sizeX;
	}

	@Override
	public int getApproxDestinationSizeY() {
		return sizeY;
	}

	private int getType(GameObject object) {
		int type = object.getType();
		if ((type >= 0 && type <= 3) || type == 9)
			return 0; // wall
		else if (type < 9)
			return 1; // deco
		else if (type == 10 || type == 11 || type == 22)
			return 2; // ground
		else
			return 3; // misc
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ObjectStrategy))
			return false;
		ObjectStrategy strategy = (ObjectStrategy) other;
		return x == strategy.x && y == strategy.y && routeType == strategy.routeType && type == strategy.type
				&& rotation == strategy.rotation && sizeX == strategy.sizeX && sizeY == strategy.sizeY
				&& accessBlockFlag == strategy.accessBlockFlag;
	}

}
