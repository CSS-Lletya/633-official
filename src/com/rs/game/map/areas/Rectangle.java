package com.rs.game.map.areas;

import com.rs.game.map.WorldTile;

public abstract class Rectangle extends Shape {

	public Rectangle(WorldTile northEast, WorldTile southWest) {
		areas(new WorldTile[] { northEast, southWest }).type(ShapeType.RECTANGLE);
	}

	@Override
	public boolean inside(WorldTile location) {
		if (getAreas()[0].getPlane() != location.getPlane() || getAreas()[1].getPlane() != location.getPlane())
			return false;
		if (getAreas()[0].getX() < location.getX() || getAreas()[1].getX() > location.getX())
			return false;

		if (getAreas()[0].getY() < location.getY() || getAreas()[1].getY() > location.getY())
			return false;

		return true;
	}
}