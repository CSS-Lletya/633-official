package com.rs.plugin.impl.regionalattributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="Tarn_lair_dogs")
public class TarnLairDogsRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 3134 && destX <= 3164 && destY >= 4640 && destY <= 4669;
	}
}