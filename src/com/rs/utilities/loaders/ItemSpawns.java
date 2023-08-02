package com.rs.utilities.loaders;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.rs.game.item.ItemSpawn;
import com.rs.utilities.GSONParser;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class ItemSpawns {

    final static Charset ENCODING = StandardCharsets.UTF_8;
    private final static String PATH = "data/items/spawns/";
    private static final ObjectArrayList<ItemSpawn> ALL_SPAWNS = new ObjectArrayList<>();
    private static final Object2ObjectOpenHashMap<Integer, ObjectArrayList<ItemSpawn>> ITEM_SPAWNS = new Object2ObjectOpenHashMap<>();

    public static void init() {
//        Logger.log("ItemSpawns", "Loading item spawns...");
        File[] spawnFiles = new File(PATH).listFiles();
        for (File f : spawnFiles) {
            ItemSpawn[] spawns = (ItemSpawn[]) GSONParser.loadFile(f.getAbsolutePath(), ItemSpawn[].class);
            if (spawns != null) {
                for (ItemSpawn spawn : spawns) {
                    if (spawn != null) {
                        ALL_SPAWNS.add(spawn);
                        ObjectArrayList<ItemSpawn> regionSpawns = ITEM_SPAWNS.get(spawn.getTile().getRegionId());
                        if (regionSpawns == null)
                            regionSpawns = new ObjectArrayList<>();
                        regionSpawns.add(spawn);
                        ITEM_SPAWNS.put(spawn.getTile().getRegionId(), regionSpawns);
                    }
                }
            }
        }
//        Logger.log("ItemSpawns", "Loaded " + ALL_SPAWNS.size() + " item spawns...");
    }

    public static void loadItemSpawns(int regionId) {
    	ObjectArrayList<ItemSpawn> spawns = ITEM_SPAWNS.get(regionId);
        if (spawns != null) {
            for (ItemSpawn spawn : spawns) {
                spawn.spawn();
            }
        }
    }
}
