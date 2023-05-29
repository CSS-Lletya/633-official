package com.rs.plugin.impl.regionalttributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {})
public class ClanWarsRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532
			|| destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631
			|| destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631;
				
	}
}