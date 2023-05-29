package com.rs.plugin.impl.regionalttributes;

import com.rs.game.map.WorldTile;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;

@RegionAttributeSignature(forceMultiRegions = {})
public class WildernessRegionAttributePlugin implements RegionAttributeListener {

	@Override
	public boolean withinMultiZonedBoundaries(WorldTile tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 3029 && destX <= 3374 && destY >= 3759 && destY <= 3903
			|| destX >= 3198 && destX <= 3380 && destY >= 3904 && destY <= 3970
			|| destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759
			|| destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759
			|| destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710
			|| destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646
			|| destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375
			|| destX >= 3195 && destX <= 3327 && destY >= 3520 && destY <= 3970;
	}
}