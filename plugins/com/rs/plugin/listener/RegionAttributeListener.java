package com.rs.plugin.listener;

import com.rs.game.map.WorldTile;

/**
 *
 * @author Dennis
 *
 */
public interface RegionAttributeListener {
	
	public boolean withinMultiZonedBoundaries(WorldTile tile);
}
