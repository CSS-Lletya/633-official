package com.rs.game.map.areas;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.rs.game.map.areas.impl.Multi;
import com.rs.game.player.Player;

/**
 * Handles a specified Area that can contain a series of events that can occur within the coordinates.
 * Example: Multi-zone updating, etc..
 * @author Dennis
 *
 */
public final class AreaHandler {

	/**
	 * An immutable list of Areas
	 */
	private static ImmutableList<Area> AREAS = ImmutableList.of(new Multi());

	/**
	 * Gets the specified area the Player is interacting with
	 * @param player
	 * @return area
	 */
	public static Optional<Area> getArea(Player player) {
		for (Area area : AREAS) {
			for (Shape shape : area.shapes()) {
				if (shape.inside(player)) {
					return Optional.of(area);
				}
			}
		}
		return Optional.absent();
	}
	
	/**
	 * Processes the current Area if present
	 * @param player
	 */
	public static void processArea(Player player) {
		if (getArea(player).isPresent())
			getArea(player).get().processArea(player);
	}
}