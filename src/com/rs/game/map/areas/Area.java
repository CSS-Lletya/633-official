package com.rs.game.map.areas;

import com.rs.game.player.Player;

/**
 * Represents an Area that the player will be interacting with
 * @author Dennis
 *
 */
public abstract class Area {

	/**
	 * A Basic name of the Area the Player will interact with
	 * (Serves no purpose yet)
	 * @return
	 */
	public abstract String name();

	/**
	 * The Shape of the Area (Polygon, Rectangle)
	 * @return shape
	 */
	public abstract Shape[] shapes();

	/**
	 * The Environment type of the Area
	 * @return environment
	 */
	public abstract AreaType environment();

	/**
	 * The Area processing event. This can be used for specific events to take place.
	 * This is also an optional void in which you can choose to utilize in the Area or not.
	 */
	public void processArea(Player player) {
		
	}
	
	/**
	 * Represents an Area's Type
	 * @author Dennis
	 */
	public enum AreaType {
		NORMAL, MULTI;
	}
}