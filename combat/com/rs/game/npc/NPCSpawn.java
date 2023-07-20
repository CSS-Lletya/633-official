// This program is free software: you can redistribute it and/or modify
package com.rs.game.npc;

import com.rs.game.map.WorldTile;
import com.rs.utilities.Direction;

public class NPCSpawn {

	private String comment;
	private int npcId;
	private WorldTile tile;
	private Direction dir;
	private String customName;

	public NPCSpawn(int npcId, WorldTile tile, Direction dir, String comment) {
		this.npcId = npcId;
		this.tile = tile;
		this.dir = dir;
		this.comment = comment;
	}

	public void spawn() {
		NPC.spawnNPC((short) npcId, tile, dir, true);
	}

	public WorldTile getTile() {
		return tile;
	}

	public int getNPCId() {
		return npcId;
	}

	public String getComment() {
		return comment;
	}

	public Direction getDir() {
		return dir;
	}

	public NPCSpawn setCustomName(String customName) {
		this.customName = customName;
		return this;
	}

	public String getCustomName() {
		return customName;
	}
}
