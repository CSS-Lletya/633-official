package com.rs.plugin.listener;

import com.rs.game.Entity;

/**
 *
 * @author Dennis
 *
 */
public interface RegionAttributeListener {
	
	public boolean withinMultiZonedBoundaries(Entity tile);
}
