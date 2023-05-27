package com.rs.game.player.actions;

import com.rs.game.player.Player;

/**
 * Represents an Action a Player creates
 * @author Dennis
 *
 */
public abstract class Action {
	
	/**
	 * Starts the Action
	 * @return action
	 */
    public abstract boolean start(Player player);

    /**
     * Processes the Action in real time
     * @return process
     */
    public abstract boolean process(Player player);

    /**
     * Process the Action with a fixed delay
     * @return fixed delayed process
     */
    public abstract int processWithDelay(Player player);

    /**
     * Stops the Action that's currently being performed
     */
    public abstract void stop(Player player);
}