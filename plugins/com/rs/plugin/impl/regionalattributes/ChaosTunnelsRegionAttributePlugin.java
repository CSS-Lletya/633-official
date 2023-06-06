package com.rs.plugin.impl.regionalattributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {}, alias="Chaos_tunnels")
public class ChaosTunnelsRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		boolean check = destX >= 3136 && destX <= 3327 && destY >= 5443 && destY <= 5571;
		return check;
	}
}