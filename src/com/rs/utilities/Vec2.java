package com.rs.utilities;

import com.rs.game.map.WorldTile;

public class Vec2 {

    private float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(WorldTile tile) {
        this.x = tile.getX();
        this.y = tile.getY();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vec2 sub(Vec2 v2) {
        return new Vec2(this.x - v2.x, this.y - v2.y);
    }

    public void norm() {
        float mag = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        this.x = (float) (this.x / mag);
        this.y = (float) (this.y / mag);
    }

    public WorldTile toTile() {
        return toTile(0);
    }

    public WorldTile toTile(int plane) {
        return new WorldTile((int) Math.round(this.x), (int) Math.round(this.y), plane);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
