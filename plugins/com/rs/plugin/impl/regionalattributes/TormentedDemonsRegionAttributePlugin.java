package com.rs.plugin.impl.regionalattributes;

import com.rs.game.Entity;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="Tormented_demons")
public class TormentedDemonsRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(Entity tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752;
	}
}