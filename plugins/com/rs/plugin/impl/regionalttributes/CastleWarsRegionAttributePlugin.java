package com.rs.plugin.impl.regionalttributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {})
public class CastleWarsRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135
			|| destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532;
	}
}