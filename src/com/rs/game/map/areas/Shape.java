package com.rs.game.map.areas;

import com.rs.game.map.WorldTile;

import lombok.Data;

@Data
public abstract class Shape {

	private WorldTile[] areas;
	private ShapeType type;

	public abstract boolean inside(WorldTile location);

	public Shape areas(WorldTile[] areas) {
		this.areas = areas;
		return this;
	}

	public Shape type(ShapeType type) {
		this.type = type;
		return this;
	}

	public enum ShapeType {
		RECTANGLE, POLYGON;
	}
}