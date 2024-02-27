package com.rs.content.mapzone.impl;

import com.rs.content.mapzone.MapZone;
import com.rs.content.mapzone.ZoneRestriction;
import com.rs.game.player.Player;

public class TestMapZone extends MapZone {

	public TestMapZone() {
		super("Test MapZone", MapZoneSafetyCondition.SAFE, MapZoneType.NORMAL, ZoneRestriction.FIRES, ZoneRestriction.CANNON);
	}

	@Override
	public void start(Player player) {
		System.out.println(getMapeZoneName());
	}

	@Override
	public void finish(Player player) {
		
	}
	
	@Override
	public boolean canMove(Player player, int dir) {
		return true;
	}
}