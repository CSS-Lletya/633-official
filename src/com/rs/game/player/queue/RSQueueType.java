package com.rs.game.player.queue;

/**
 * The {@code RSQueueType} determines how an {@code RSScript} will be processed,
 * or if it will be processed depending on which other type is currently in the
 * {@code RSScriptQueue}.
 * 
 * <a href=https://osrs-docs.com/docs/mechanics/queues/>Kris OSRS-Docs</a>
 * 
 * @author Albert Beaupre
 */
public enum RSQueueType {
	/**
	 * <p>
	 * <ul>
	 * <li>Removed from the queue if there are any strong scripts in the queue prior
	 * to the queue being processed.</li>
	 * <li>Removed from the queue upon any interruptions, some of which are:
	 * <ul>
	 * <li>Interacting with an entity or clicking on a game square.</li>
	 * <li>Interacting with an item in your inventory.</li>
	 * <li>Unequipping an item.</li>
	 * <li>Opening an interface.</li>
	 * <li>Closing an interface.</li>
	 * <li>Dragging items in inventory.</li>
	 * </ul>
	 * </li>
	 * <li>In general, it seems like any action which closes an interface also
	 * clears all weak scripts from the queue.</li>
	 * </ul>
	 */
	Weak,

	/**
	 * <li>Skipped in the execution block if the player has a modal interface open
	 * at the time.</li>
	 */
	Normal,

	/**
	 * <ul>
	 * <li>Removes all weak scripts from the queue prior to being processed.</li>
	 * <li>Closes modal interface prior to executing.</li>
	 * </ul>
	 */
	Strong,

	/**
	 * <ul>
	 * <li>Cannot be paused or interrupted. It will always execute as long as the
	 * timer behind it is up.</li>
	 * <li>Closes modal interface prior to executing.</li>
	 * </ul>
	 */
	Soft,

	/**
	 * Custom script type. This type of script interrupts all weak scripts currently
	 * in the queue, and are also processed instantly. This type is still affected
	 * by delays and suspends and will be removed from the queue by a strong script.
	 */
	Instant
}