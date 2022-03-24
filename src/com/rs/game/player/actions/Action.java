package com.rs.game.player.actions;

import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.player.Player;

import lombok.Data;
import lombok.Getter;

/**
 * Represents an Action a Player creates
 * @author Dennis
 *
 */
@Data
public abstract class Action {

	/**
	 * The Player
	 */
	@Getter
	private final Player player;
	
	/**
	 * The Target the {@link #player} will be interacting with
	 */
	@Getter
	private  final Optional<Entity> target;
	
	/**
	 * Starts the Action
	 * @return action
	 */
    public abstract boolean start();

    /**
     * Processes the Action in real time
     * @return process
     */
    public abstract boolean process();

    /**
     * Process the Action with a fixed delay
     * @return fixed delayed process
     */
    public abstract int processWithDelay();

    /**
     * Stops the Action that's currently being performed
     */
    public abstract void stop();
}