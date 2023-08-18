package com.rs.game.movement.route.strategy;

import java.util.ArrayDeque;
import java.util.Deque;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;

public final class DumbRouteFinder {

    public static boolean addDumbPathfinderSteps(Entity entity, WorldTile target) {
        return addDumbPathfinderSteps(entity, target, 25);
    }

    public static boolean addDumbPathfinderSteps(Entity entity, WorldTile target, int maxSize) {
        Deque<WorldTile> tiles = find(entity, target, maxSize);
        if (tiles.size() > 0) {
            WorldTile last = new WorldTile(entity);
            for (WorldTile t : tiles) {
                entity.addWalkStep(t.getX(), t.getY(), last.getX(), last.getY(), true);
                last = t;
            }
            return true;
        }
        return false;
    }

    private static Deque<WorldTile> find(WorldTile origin, WorldTile target, int maxSize) {
        int size = origin instanceof Entity ? ((Entity)origin).getSize() : 1;
        WorldTile real = new WorldTile(origin);
        WorldTile curr = origin instanceof Entity ? ((Entity)origin).getMiddleWorldTile() : new WorldTile(origin);
        WorldTile targ = target instanceof Entity ? ((Entity)target).getMiddleWorldTile() : new WorldTile(target);
        Deque<WorldTile> positions = new ArrayDeque<>(maxSize);
        while (true) {
            WorldTile from = new WorldTile(curr);
            if (curr.getX() < targ.getX() && curr.getY() < targ.getY()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(1, 1), size)) {
                    real = add(positions, real.transform(1, 1));
                    curr = curr.transform(1, 1);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(1, 0), size)) {
                    real = add(positions, real.transform(1, 0));
                    curr = curr.transform(1, 0);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(0, 1), size)) {
                    real = add(positions, real.transform(0, 1));
                    curr = curr.transform(0, 1);
                }
            } else if (curr.getX() > targ.getX() && curr.getY() > targ.getY()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(-1, -1), size)) {
                    real = add(positions, real.transform(-1, -1));
                    curr = curr.transform(-1, -1);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(-1, 0), size)) {
                    real = add(positions, real.transform(-1, 0));
                    curr = curr.transform(-1, 0);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(0, -1), size)) {
                    real = add(positions, real.transform(0, -1));
                    curr = curr.transform(0, -1);
                }
            } else if (curr.getX() < targ.getX() && curr.getY() > targ.getY()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(1, -1), size)) {
                    real = add(positions, real.transform(1, -1));
                    curr = curr.transform(1, -1);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(1, 0), size)) {
                    real = add(positions, real.transform(1, 0));
                    curr = curr.transform(1, 0);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(0, -1), size)) {
                    real = add(positions, real.transform(0, -1));
                    curr = curr.transform(0, -1);
                }
            } else if (curr.getX() > targ.getX() && curr.getY() < targ.getY()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(-1, 1), size)) {
                    real = add(positions, real.transform(-1, 1));
                    curr = curr.transform(-1, 1);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(-1, 0), size)) {
                    real = add(positions, real.transform(-1, 0));
                    curr = curr.transform(-1, 0);
                } else if (World.getTileAttributes().checkWalkStep(real, real.transform(0, 1), size)) {
                    real = add(positions, real.transform(0, 1));
                    curr = curr.transform(0, 1);
                }
            } else if (curr.getX() < targ.getX()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(1, 0), size)) {
                    real = add(positions, real.transform(1, 0));
                    curr = curr.transform(1, 0);
                }
            } else if (curr.getX() > targ.getX()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(-1, 0), size)) {
                    real = add(positions, real.transform(-1, 0));
                    curr = curr.transform(-1, 0);
                }
            } else if (curr.getY() < targ.getY()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(0, 1), size)) {
                    real = add(positions, real.transform(0, 1));
                    curr = curr.transform(0, 1);
                }
            } else if (curr.getY() > targ.getY()) {
                if (World.getTileAttributes().checkWalkStep(real, real.transform(0, -1), size)) {
                    real = add(positions, real.transform(0, -1));
                    curr = curr.transform(0, -1);
                }
            }
            if (curr.matches(from))
                break;
        }
        return positions;
    }

    private static WorldTile add(Deque<WorldTile> positions, WorldTile att) {
        positions.add(att);
        return att;
    }
}