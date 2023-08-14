// This program is free software: you can redistribute it and/or modify
package com.rs.game.npc.drops;

import java.io.File;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utilities.GSONParser;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class DropSets {

	private final static String PATH = "data/npcs/drops/";
	public static Object2ObjectArrayMap<String, DropSet> DROPS = new Object2ObjectArrayMap<>();
	public static Object2ObjectArrayMap<Object, DropSet> NPC_DROPS = new Object2ObjectArrayMap<>();
	public static final DropSet DEFAULT_DROPSET = new DropSet(new DropTable(0.0, 0.0, false));

	public static final void init() {
		loadPackedNPCDrops();
	}

	public static void reloadDrops() {
		NPC_DROPS.clear();
		DROPS.clear();
		loadPackedNPCDrops();
	}

	public static DropSet getDropSet(String string) {
		if (NPC_DROPS.get(string) != null)
			return NPC_DROPS.get(string);
		return null;
	}

	public static DropSet getDropSet(int npcId) {
		if (NPC_DROPS.get(npcId) != null)
			return NPC_DROPS.get(npcId);
		if (NPC_DROPS.get(NPCDefinitions.getNPCDefinitions(npcId).getName()) != null)
			return NPC_DROPS.get(NPCDefinitions.getNPCDefinitions(npcId).getName());
		return DEFAULT_DROPSET;
	}

	private static void loadPackedNPCDrops() {
		try {
			File[] dropFiles = new File(PATH).listFiles();
			for (File f : dropFiles)
				loadFile(f);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void loadFile(File f) {
		if (f.isDirectory()) {
			for (File dir : f.listFiles())
				loadFile(dir);
			return;
		}
		DropSet table = (DropSet) GSONParser.loadFile(f.getAbsolutePath(), DropSet.class);
		if (table != null) {
			table.getDropList();
			if (table.isOverflowed())
				System.err.println(f.getName() + " is overflowed by " + table.getDropList().getOverflow());
			DROPS.put(f.getName(), table);
			if (table.getIds() != null)
				for (int id : table.getIds())
					NPC_DROPS.put(id, table);
			if (table.getNames() != null)
				for (String name : table.getNames())
					NPC_DROPS.put(name, table);
		}
	}

	public Object2ObjectArrayMap<Object, DropSet> getDropMap() {
		return NPC_DROPS;
	}
}