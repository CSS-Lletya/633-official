// This program is free software: you can redistribute it and/or modify
package com.rs.utilities.loaders;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.game.npc.NPCSpawn;
import com.rs.utilities.json.GSONParser;

public final class NPCSpawns {

	private final static String PATH = "data/npcs/spawns/";
	final static Charset ENCODING = StandardCharsets.UTF_8;

	private static final ArrayList<NPCSpawn> ALL_SPAWNS = new ArrayList<>();
	private static final Map<Integer, List<NPCSpawn>> NPC_SPAWNS = new HashMap<>();
	
	public static final void init() {
		File[] spawnFiles = new File(PATH).listFiles();
		for (File f : spawnFiles) {
			if (f.getName().startsWith("_"))
				continue;
			NPCSpawn[] spawns = (NPCSpawn[]) GSONParser.loadFile(f.getAbsolutePath(), NPCSpawn[].class);
			if (spawns != null)
				for(NPCSpawn spawn : spawns)
					if (spawn != null)
						add(spawn);
		}
	}

	public static void add(NPCSpawn spawn) {
		if (spawn != null) {
			ALL_SPAWNS.add(spawn);
			List<NPCSpawn> regionSpawns = NPC_SPAWNS.get(spawn.getTile().getRegionId());
			if (regionSpawns == null)
				regionSpawns = new ArrayList<>();
			regionSpawns.add(spawn);
			NPC_SPAWNS.put(spawn.getTile().getRegionId(), regionSpawns);
		}
	}

	public static List<NPCSpawn> getAllSpawns() {
		return ALL_SPAWNS;
	}

	public static void loadNPCSpawns(int regionId) {
		List<NPCSpawn> spawns = NPC_SPAWNS.get(regionId);
		if (spawns != null)
			for (NPCSpawn spawn : spawns)
				spawn.spawn();
	}
}
