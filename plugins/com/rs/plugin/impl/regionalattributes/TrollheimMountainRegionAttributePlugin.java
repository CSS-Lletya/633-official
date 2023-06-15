package com.rs.plugin.impl.regionalattributes;

import com.rs.game.Entity;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="Trollheim_mountain")
public class TrollheimMountainRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(Entity tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2815 && destX <= 2949 && destY >= 3576 && destY <= 3727;
	}
}