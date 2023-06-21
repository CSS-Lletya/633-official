package com.rs.utilities;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.game.item.ItemSpawn;

public final class ItemSpawns {

    final static Charset ENCODING = StandardCharsets.UTF_8;
    private final static String PATH = "data/items/spawns/";
    private static final ArrayList<ItemSpawn> ALL_SPAWNS = new ArrayList<>();
    private static final Map<Integer, List<ItemSpawn>> ITEM_SPAWNS = new HashMap<>();

    public static void init() {
//        Logger.log("ItemSpawns", "Loading item spawns...");
        File[] spawnFiles = new File(PATH).listFiles();
        for (File f : spawnFiles) {
            ItemSpawn[] spawns = (ItemSpawn[]) GSONParser.loadFile(f.getAbsolutePath(), ItemSpawn[].class);
            if (spawns != null) {
                for (ItemSpawn spawn : spawns) {
                    if (spawn != null) {
                        ALL_SPAWNS.add(spawn);
                        List<ItemSpawn> regionSpawns = ITEM_SPAWNS.get(spawn.getTile().getRegionId());
                        if (regionSpawns == null)
                            regionSpawns = new ArrayList<>();
                        regionSpawns.add(spawn);
                        ITEM_SPAWNS.put(spawn.getTile().getRegionId(), regionSpawns);
                    }
                }
            }
        }
//        Logger.log("ItemSpawns", "Loaded " + ALL_SPAWNS.size() + " item spawns...");
    }

    public static void loadItemSpawns(int regionId) {
        List<ItemSpawn> spawns = ITEM_SPAWNS.get(regionId);
        if (spawns != null) {
            for (ItemSpawn spawn : spawns) {
                spawn.spawn();
            }
        }
    }
}
