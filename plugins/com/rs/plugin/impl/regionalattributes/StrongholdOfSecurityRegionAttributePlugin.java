package com.rs.plugin.impl.regionalattributes;

import com.rs.game.Entity;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="Stronghold_of_security")
public class StrongholdOfSecurityRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(Entity tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 1853 && destX <= 1919 && destY >= 5184 && destY <= 5250;
	}
}