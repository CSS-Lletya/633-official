package com.rs.net.wordlist;

import java.util.HashMap;

public class WorldList {

	public static final HashMap<Integer, WorldEntry> WORLDS = new HashMap<Integer, WorldEntry>();

	public static void init() {
		WORLDS.put(1, new WorldEntry("Local Development", "127.0.0.1", 37, "Main", true)); //Local development world
		for (int i = 2; i <= 199; i++) {
			WORLDS.put(i, new WorldEntry("World " + i, "127.0.0.1", i, "n/a", (i % 2 == 0))); //WORLD randomizing kek
		}
	}

	public static WorldEntry getWorld(int worldId) {
		return WORLDS.get(worldId);
	}
}