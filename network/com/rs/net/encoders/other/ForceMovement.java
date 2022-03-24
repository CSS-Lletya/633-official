package com.rs.net.encoders.other;

import com.rs.game.map.WorldTile;
import com.rs.utilities.Utility;

import lombok.Data;

@Data
public class ForceMovement {

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	private final WorldTile toFirstTile;
	private final WorldTile toSecondTile;
	private final int firstTileTicketDelay;
	private final int secondTileTicketDelay;
	protected final int direction;

	public int getDirection() {
		switch (direction) {
		case NORTH:
			return Utility.getFaceDirection(0, 1);
		case EAST:
			return Utility.getFaceDirection(1, 0);
		case SOUTH:
			return Utility.getFaceDirection(0, -1);
		case WEST:
			return Utility.getFaceDirection(-1, 0);
		}
		return direction;
	}
}