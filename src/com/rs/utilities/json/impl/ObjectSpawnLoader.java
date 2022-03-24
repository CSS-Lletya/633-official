package com.rs.utilities.json.impl;

import java.io.File;
import java.io.FileReader;

import com.google.gson.reflect.TypeToken;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.utilities.json.GsonHandler;
import com.rs.utilities.json.GsonLoader;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class ObjectSpawnLoader extends GsonLoader<ObjectSpawnLoader.ObjectSpawn> {

	@Override
	public void initialize() {
		ObjectArrayList<ObjectSpawn> spawns = load();
		for (ObjectSpawn spawn : spawns) {
			ObjectArrayList<ObjectSpawn> regionSpawns = null;

			/* Populating the region spawns or generating a new one if it doesnt exist */
			if (map.get(spawn.getTile().getRegionId()) == null) {
				regionSpawns = new ObjectArrayList<>();
			} else {
				regionSpawns = (ObjectArrayList<ObjectSpawn>) map.get(spawn.getTile().getRegionId());
			}

			regionSpawns.add(spawn);
			map.put(spawn.getTile().getRegionId(), regionSpawns);
		}
	}

	@Override
	public String getFileLocation() {
		return "data/json/objectspawns.json";
	}

	@Override
	@SneakyThrows(Exception.class)
	public ObjectArrayList<ObjectSpawn> load() {
		ObjectArrayList<ObjectSpawn> autospawns = null;
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

		autospawns = gson.fromJson(json, new TypeToken<ObjectArrayList<ObjectSpawn>>() {
		}.getType());
		return autospawns;
	}

	public ObjectArrayList<ObjectSpawn> getSpawns(int regionId) {
		return map.get(regionId);
	}

	private Object2ObjectOpenHashMap<Integer, ObjectArrayList<ObjectSpawn>> map = new Object2ObjectOpenHashMap<>();

	public static final void loadObjectSpawns(int regionId) {
		GsonHandler.waitForLoad();
		ObjectSpawnLoader loader = GsonHandler.getJsonLoader(ObjectSpawnLoader.class);
		ObjectArrayList<ObjectSpawn> spawns = loader.getSpawns(regionId);
		if (spawns == null) {
			return;
		}
		for (ObjectSpawn spawn : spawns) {
			GameObject.spawnObject(new GameObject(spawn.getId(), (short) spawn.getType(), (short) spawn.getRotation(),
					spawn.getX(), spawn.getY(), spawn.getPlane()));
		}
	}

	@Data
	public static class ObjectSpawn {

		public ObjectSpawn(int id, int type, int rotation, int x, int y, int plane, boolean clip) {
			this.id = id;
			this.type = type;
			this.rotation = rotation;
			this.x = x;
			this.y = y;
			this.plane = plane;
			this.clip = clip;
		}

		private final int id;
		private final int type;
		private final int rotation;
		private final int x;
		private final int y;
		private final int plane;

		private final boolean clip;

		public WorldTile getTile() {
			return new WorldTile(getX(), getY(), getPlane());
		}
	}
}