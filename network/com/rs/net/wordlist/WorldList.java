package com.rs.net.wordlist;

import java.util.HashMap;

import com.rs.GameConstants;

public class WorldList {

	public static final HashMap<Integer, WorldEntry> WORLDS = new HashMap<Integer, WorldEntry>();

	public static void init() {
		WORLDS.put(1, new WorldEntry(GameConstants.SERVER_NAME, "127.0.0.1", 37, "Main", false)); //WORLD #1
		WORLDS.put(2, new WorldEntry("Beta", "127.0.0.1", 1, "Beta", true)); //WORLD #2
	}

	public static WorldEntry getWorld(int worldId) {
		return WORLDS.get(worldId);
	}
}