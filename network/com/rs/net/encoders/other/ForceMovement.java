package com.rs.net.encoders.other;

import com.rs.game.map.WorldTile;
import com.rs.utilities.Direction;
import com.rs.utilities.Utility;

import lombok.Data;

@Data
public class ForceMovement {
	
    protected Direction direction;
    private WorldTile toFirstTile;
    private WorldTile toSecondTile;
    private int firstTileTicketDelay;
    private int secondTileTicketDelay;

    public ForceMovement(WorldTile toFirstTile, int firstTileTicketDelay,
    		Direction direction) {
        this(toFirstTile, firstTileTicketDelay, null, 0, direction);
    }

    public ForceMovement(WorldTile toFirstTile, int firstTileTicketDelay, WorldTile toSecondTile, int secondTileTicketDelay, Direction direction) {
        this.toFirstTile = toFirstTile;
        this.firstTileTicketDelay = firstTileTicketDelay;
        this.toSecondTile = toSecondTile;
        this.secondTileTicketDelay = secondTileTicketDelay;
        this.direction = direction;
    }

    public int getDirection() {
        switch (direction) {
            case NORTH:
                return Utility.getFaceDirection(0, 1);
            case EAST:
                return Utility.getFaceDirection(1, 0);
            case SOUTH:
                return Utility.getFaceDirection(0, -1);
            case WEST:
            default:
                return Utility.getFaceDirection(-1, 0);
        }
    }
}