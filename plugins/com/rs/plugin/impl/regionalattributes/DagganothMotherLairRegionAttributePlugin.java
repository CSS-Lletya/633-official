package com.rs.plugin.impl.regionalattributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {10140}, alias="Dagganoth_mother_lair")
public class DagganothMotherLairRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		return false;
	}
}