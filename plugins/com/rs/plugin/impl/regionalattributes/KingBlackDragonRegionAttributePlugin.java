package com.rs.plugin.impl.regionalattributes;

import com.rs.game.Entity;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="King_black_dragon")
public class KingBlackDragonRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(Entity tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720;
	}
}