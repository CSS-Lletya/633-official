package com.rs.game.map;

import com.rs.GameConstants;
import com.rs.game.Entity;
import com.rs.utilities.RandomUtility;

import lombok.Data;

@Data
public class WorldTile {

	private short x, y;
	private byte plane;
	
	public WorldTile(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public WorldTile(int x, int y, int plane, int size) {
		this.x = (short) getCoordFaceX(x, size, size, -1);
		this.y = (short) getCoordFaceY(y, size, size, -1);
		this.plane = (byte) plane;
	}

	public WorldTile(WorldTile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.plane = tile.plane;
	}
	
	public WorldTile(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
	}

	public WorldTile(WorldTile tile, int randomize) {
		this.x = (short) (tile.x + RandomUtility.inclusive(randomize * 2) - randomize);
		this.y = (short) (tile.y + RandomUtility.inclusive(randomize * 2) - randomize);
		this.plane = tile.plane;
	}

	public WorldTile(int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.plane = (byte) (hash >> 28);
	}

	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		plane += planeOffset;
	}

	public final void setLocation(WorldTile tile) {
		setLocation(tile.x, tile.y, tile.plane);
	}

	public final void setLocation(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getXInChunk() {
		return x & 0x7;
	}

	public int getYInChunk() {
		return y & 0x7;
	}

	public int getPlane() {
		if (plane > 3)
			return 3;
		return plane;
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getLocalX(WorldTile tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (GameConstants.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalY(WorldTile tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (GameConstants.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalX(WorldTile tile) {
		return getLocalX(tile, 0);
	}

	public int getLocalY(WorldTile tile) {
		return getLocalY(tile, 0);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (plane << 16);
	}

	public int getTileHash() {
		return y + (x << 14) + (plane << 28);
	}

	public boolean withinDistance(WorldTile tile, int distance) {
		if (tile.plane != plane)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
	}

	public boolean withinDistance(WorldTile tile) {
		if (tile.plane != plane)
			return false;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;
	}

	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public static final int getCoordFaceX(int x, int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static final int getCoordFaceY(int y, int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}
	
	/**
	 * Checks if this world tile's coordinates match the other world tile.
	 * 
	 * @param other The world tile to compare with.
	 * @return {@code True} if so.
	 */
	public boolean matches(WorldTile other) {
		return x == other.x && y == other.y && plane == other.plane;
	}

	public boolean withinArea(int a, int b, int c, int d) {
		return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
	}
	
	public WorldTile transform(int x, int y) {
		return transform(x, y, 0);
	}

	public WorldTile transform(int x, int y, int plane) {
		return new WorldTile(this.x + x, this.y + y, this.plane + plane);
	}

	public static boolean collides(Entity entity, Entity target) {
		return entity.getPlane() == target.getPlane() && collides(entity.getX(), entity.getY(), entity.getSize(), target.getX(), target.getY(), target.getSize());
	}
	
	public static boolean collides(int x1, int y1, int size1, int x2, int y2, int size2) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		return distanceX < size2 && distanceX > -size1 && distanceY < size2 && distanceY > -size1;
	}
	
	public int getDistance(final WorldTile from) {
		final int distanceX = Math.abs(from.getX() - getX());
		final int distanceY = Math.abs(from.getY() - getY());
		return (int) Math.ceil((distanceX + distanceY) / 2);
	}

	public int getFurthestDistance(final WorldTile from) {
		final int distanceX = Math.abs(from.getX() - getX());
		final int distanceY = Math.abs(from.getY() - getY());
		return distanceX > distanceY ? distanceX : distanceY;
	}

	/**
	 * Creates a backing copy of this position.
	 */
	public WorldTile copy() {
		return new WorldTile(x, y, plane);
	}

	/**
	 * Returns the delta coordinates. Note that the returned position is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * @param a the first position.
	 * @param b the second position.
	 * @return the delta coordinates contained within a position.
	 */
	public static WorldTile delta(WorldTile a, WorldTile b) {
		return new WorldTile(b.x - a.x, b.y - a.y, 0);
	}
	
	/**
	 * Gets the current position and increases the plane height by 1
	 * @return
	 */
	public WorldTile increasePlane() {
		return new WorldTile(x, y, plane + 1);
	}
	
	/**
	 * Gets the current position and decreases the plane height by 1
	 * @return
	 */
	public WorldTile decreasePlane() {
		return new WorldTile(x, y, plane - 1);
	}
}