package com.rs.game.movement.route.strategy;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

import java.util.function.Consumer;

/**
 * Creates a more accurate Object interaction compared to RS.
 * Before force movements would look like ass if you were on the perfect tile,
 * this fixes that issue neatly and quite simple to operate.
 * NOTE: Subject to change were improvements can be made.
 */
public class PreciseInteraction extends Action {

    /**
     * The event to take place when the player reaches specified target tile.
     */
    private final Consumer<Player> consumer;

    /**
     * Represents the Tile we're trying to interact correctly with when reached.
     */
    private final WorldTile tile;

    /**
     * Constructs a precise movement
     * @param player
     * @param tile
     * @param consumer
     */
    public PreciseInteraction(WorldTile object, Consumer<Player> consumer) {
        this.tile = object;
        this.consumer = consumer;
    }

    @Override
    public boolean start(Player player) {
        player.addWalkSteps(tile.getX(), tile.getY());
        return process(player);
    }

    @Override
    public boolean process(Player player) {
        if (player.matches(tile)) {
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