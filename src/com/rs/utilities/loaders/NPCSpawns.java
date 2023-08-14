// This program is free software: you can redistribute it and/or modify
package com.rs.utilities.loaders;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.rs.GameConstants;
import com.rs.game.npc.NPCSpawn;
import com.rs.utilities.GSONParser;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public final class NPCSpawns {

	private final static String PATH = "data/npcs/spawns/";
	final static Charset ENCODING = StandardCharsets.UTF_8;

	private static final ObjectArrayList<NPCSpawn> ALL_SPAWNS = new ObjectArrayList<>();
	private static final Short2ObjectOpenHashMap<ObjectArrayList<NPCSpawn>> NPC_SPAWNS = new Short2ObjectOpenHashMap<>();
	
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
			ObjectArrayList<NPCSpawn> regionSpawns = NPC_SPAWNS.get((short) spawn.getTile().getRegionId());
			if (regionSpawns == null)
				regionSpawns = new ObjectArrayList<>();
			regionSpawns.add(spawn);
			NPC_SPAWNS.put((short) spawn.getTile().getRegionId(), regionSpawns);
		}
	}

	public static ObjectArrayList<NPCSpawn> getAllSpawns() {
		return ALL_SPAWNS;
	}

	public static void loadNPCSpawns(int regionId) {
		ObjectArrayList<NPCSpawn> spawns = NPC_SPAWNS.get((short) regionId);
		if (spawns != null)
			for (NPCSpawn spawn : spawns) {
				if (!GameConstants.isPVPWorld() && spawn.getNPCId() == 8725) 
					continue;
				spawn.spawn();
			}
	}
}
