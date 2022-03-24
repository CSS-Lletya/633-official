package com.rs.game.player;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Data;

/**
 * Represents an Attribute that can be bound to a Enity (Player, NPC)
 * @author Dennis
 *
 */
@Data
public class Attributes {
	
	/**
	 * A Temporary attribute map for an Entity;
	 */
	private transient Object2ObjectOpenHashMap<Object, Object> attributes = new Object2ObjectOpenHashMap<>();
	
}