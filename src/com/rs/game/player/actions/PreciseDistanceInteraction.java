package com.rs.game.player.actions;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import java.util.function.Consumer;

/**
 * Creates a more accurate Object interaction compared to RS.
 * Before force movements would look like ass if you were on the perfect tile,
 * this fixes that issue neatly and quite simple to operate.
 * NOTE: Subject to change were improvements can be made.
 */
public class PreciseDistanceInteraction extends Action {

    /**
     * The event to take place when the player reaches specified target tile.
     */
    private final Consumer<Player> consumer;

    /**
     * Represents the Tile we're trying to interact correctly with when reached.
     */
    private final WorldTile tile;

    /**
     * The exact distance the event should execute on
     * (Tiles from object, if meets criteria then we execute)
     */
    private final byte distance;

    /**
     * Type attributes. (Bit weird to explain, but do try this yourself if you'd like!)
     * Smart: Used for distanced interactions & clipping (example: Telegrab Packet). Tile checking
     * is less accurate than Basic has (AKA the 2 different Types that exist).
     *
     * Basic: Used for standard essential interactions, this however is more precise than Smart for
     * precise tile requirements.
     */
    public enum Type {
        SMART, BASIC
    }

    private final Enum<Type> type;

    /**
     * Constructs a precise movement
     * @param player
     * @param tile
     * @param consumer
     */
    public PreciseDistanceInteraction(WorldTile object, byte distance, Enum<Type> type, Consumer<Player> consumer) {
        this.tile = object;
        this.distance = distance;
        this.type = type;
        this.consumer = consumer;
    }

    @Override
    public boolean start(Player player) {
        if (type == Type.SMART)
            player.calcFollow(tile, true);
        else
            player.addWalkSteps(tile.getX(), tile.getY());
        return process(player);
    }

    @Override
    public boolean process(Player player) {
        if (player.withinDistance(tile, distance)) {
            consumer.accept(player);
            return false;
        }
        return true;
    }

    @Override
    public int processWithDelay(Player player) {
        return 0;
    }

    @Override
    public void stop(Player player) {  }

}
