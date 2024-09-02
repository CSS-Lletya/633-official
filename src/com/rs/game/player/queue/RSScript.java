package com.rs.game.player.queue;

/**
 * The {@code RSScript} is processed from an {@code RSScriptQueue} and is meant
 * for players and npc script processing.
 * 
 * @author Albert Beaupre
 * @param <O> The type of owner
 */
public interface RSScript<O> {

	/**
	 * Processes this {@code RSScript} for the given {@code owner}.
	 * 
	 * @param owner the owner of the script
	 */
	public abstract void process(O owner);

	/**
	 * Returns the {@code RSQueueType} for this {@code RSScript}.
	 * 
	 * @return the script type
	 */
	public abstract RSQueueType type();

}
