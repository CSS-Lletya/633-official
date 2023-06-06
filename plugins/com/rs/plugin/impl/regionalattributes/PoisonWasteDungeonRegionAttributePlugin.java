package com.rs.plugin.impl.regionalattributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="Poison_waste_dungeon")
public class PoisonWasteDungeonRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 1986 && destX <= 2045 && destY >= 4162 && destY <= 4286;
	}
}