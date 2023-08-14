package com.rs.utilities.loaders;

import java.io.File;

import com.rs.game.item.ItemSpawn;
import com.rs.utilities.GSONParser;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public final class ItemSpawns {

    private final static String PATH = "data/items/spawns/";
    private static final ObjectArrayList<ItemSpawn> ALL_SPAWNS = new ObjectArrayList<>();
    private static final Short2ObjectOpenHashMap<ObjectArrayList<ItemSpawn>> ITEM_SPAWNS = new Short2ObjectOpenHashMap<>();

    public static void init() {
        File[] spawnFiles = new File(PATH).listFiles();
        for (File f : spawnFiles) {
            ItemSpawn[] spawns = (ItemSpawn[]) GSONParser.loadFile(f.getAbsolutePath(), ItemSpawn[].class);
            if (spawns != null) {
                for (ItemSpawn spawn : spawns) {
                    if (spawn != null) {
                        ALL_SPAWNS.add(spawn);
                        ObjectArrayList<ItemSpawn> regionSpawns = ITEM_SPAWNS.get((short) spawn.getTile().getRegionId());
                        if (regionSpawns == null)
                            regionSpawns = new ObjectArrayList<>();
                        regionSpawns.add(spawn);
                        ITEM_SPAWNS.put((short) spawn.getTile().getRegionId(), regionSpawns);
                    }
                }
            }
        }
    }

    public static void loadItemSpawns(int regionId) {
    	ObjectArrayList<ItemSpawn> spawns = ITEM_SPAWNS.get((short) regionId);
        if (spawns != null) {
            for (ItemSpawn spawn : spawns) {
                spawn.spawn();
            }
        }
    }
}
