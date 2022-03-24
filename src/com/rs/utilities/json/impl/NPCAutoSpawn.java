package com.rs.utilities.json.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.rs.cache.Cache;
import com.rs.game.npc.NPC;
import com.rs.utilities.json.GsonHandler;
import com.rs.utilities.json.GsonLoader;
import com.rs.utilities.loaders.NPCSpawning;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.SneakyThrows;

public class NPCAutoSpawn extends GsonLoader<NPCSpawning> {

	public static void main(String... args) throws IOException {
		Cache.init();
		GsonHandler.initialize();
		NPCAutoSpawn loader = GsonHandler.getJsonLoader(NPCAutoSpawn.class);

		ObjectArrayList<NPCSpawning> spawns = loader.load();

		BufferedReader reader = new BufferedReader(new FileReader("drops.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] data = line.split("-");
			short id = Short.parseShort(data[0]);
			int x = Integer.parseInt(data[1]);
			int y = Integer.parseInt(data[2]);
			int z = Integer.parseInt(data[3]);
			spawns.add(new NPCSpawning(x, y, z, id, Direction.NORTH));
		}
		loader.save((ObjectArrayList<NPCSpawning>) spawns);
		reader.close();
		 
		/*
		ListIterator<NPCSpawning> it = spawns.listIterator();
		int[] regionids = { 12190, 12446, 12445 };
		while (it.hasNext()) {
			NPCSpawning spawn = it.next();
			String name = NPCDefinitions.getNPCDefinitions(spawn.getId()).getName();
			for (int id : regionids) {
				if (new WorldTile(spawn.getX(), spawn.getY(), spawn.getZ()).getRegionId() == id && !name.toLowerCase().contains("revenant")) {
					dumpDrop(spawn, name);
				}
			}
		}*/
	}

	@Override
	public void initialize() {
		ObjectArrayList<NPCSpawning> spawns = load();
		for (NPCSpawning spawn : spawns) {
			ObjectArrayList<NPCSpawning> regionSpawns = null;

			/*
			 * Populating the region spawns or generating a new one if it doesnt
			 * exist
			 */
			if (map.get(spawn.getTile().getRegionId()) == null) {
				regionSpawns = new ObjectArrayList<>();
			} else {
				regionSpawns = (ObjectArrayList<NPCSpawning>) map.get(spawn.getTile().getRegionId());
			}

			regionSpawns.add(spawn);
			map.put(spawn.getTile().getRegionId(), regionSpawns);
		}
	}

	@Override
	public String getFileLocation() {
		return "./data/json/npcspawns.json";
	}

	@Override
	@SneakyThrows(Exception.class)
	public ObjectArrayList<NPCSpawning> load() {
		ObjectArrayList<NPCSpawning> autospawns = null;
		String json = null;
		File file = new File(getFileLocation());
		if (!file.exists()) {
			return null;
		}
		FileReader reader = new FileReader(file);
		char[] chars = new char[(int) file.length()];
		reader.read(chars);
		json = new String(chars);
		reader.close();
		
		autospawns = gson.fromJson(json, new TypeToken<ObjectArrayList<NPCSpawning>>() {
		}.getType());
		return autospawns;
	}

	public Direction getDirection(NPC npc) {
		ObjectArrayList<NPCSpawning> spawns = getSpawns(npc.getRegionId());
		if (spawns == null) {
			return Direction.NORTH;
		}
		for (NPCSpawning npcSpawning : spawns) {
			if (npcSpawning.getId() == npc.getId() && npcSpawning.getX() == npc.getLastWorldTile().getX() && npcSpawning.getY() == npc.getLastWorldTile().getY() && npcSpawning.getZ() == npc.getLastWorldTile().getPlane()) {
				return npcSpawning.getDirection();
			}
		}
		return Direction.NORTH;
	}

	public ObjectArrayList<NPCSpawning> getSpawns(int regionId) {
		return (ObjectArrayList<NPCSpawning>) map.get(regionId);
	}

	private final Object2ObjectOpenHashMap<Integer, List<NPCSpawning>> map = new Object2ObjectOpenHashMap<>();

	public enum Direction {

		NORTH(0), SOUTH(4), EAST(2), WEST(6);

		private int value;

		Direction(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Direction getDirection(String text) {
			for (Direction d : Direction.values()) {
				if (d.name().equalsIgnoreCase(text)) {
					return d;
				}
			}
			return null;
		}
	}

}