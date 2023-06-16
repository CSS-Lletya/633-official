package com.rs.game.player.content.traveling;

import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

/**
 * Represents an enum of glider locations.
 * @author 'Vexia 
 * 
 */
public enum Gliders {
	TA_QUIR_PRIW(16, new WorldTile(2465, 3501, 3), 9, 3811),
	SINDARPOS(17,  new WorldTile(2848, 3497, 0), 1, 3810),
	LEMANTO_ADRA(18, new WorldTile(3321, 3427, 0), 10, -1),
	KAR_HEWO(19, new WorldTile(3278, 3212, 0), 4, 3809),
	LEMANTOLLY_UNDRI(20, new WorldTile(2544, 2970, 0), 10, 1800),
	GANDIUS(15, new WorldTile(2894, 2730, 0), 8, 3811);
	
	/**
	 * The button of the WorldTile.
	 */
	private final int button;
	
	/**
	 * The WorldTile to fly to.
	 */
	private final WorldTile WorldTile;
	
	/**
	 * The config value.
	 */
	private final int config;
	
	/**
	 * The npc.
	 */
	private final int npc;

	/**
	 * Constructs a new {@code Gliders.java} {@Code Object}
	 * @param button the button.
	 * @param WorldTile the WorldTile.
	 * @param config the config.
	 * @param npc the npc.
	 */
	Gliders(int button, WorldTile WorldTile, int config, int npc) {
		this.button = button;
		this.WorldTile = WorldTile;
		this.config = config;
		this.npc = npc;
	}
	

	/**
	 * Sends the config.
	 * @param asNpc the npc.
	 * @param player the player.
	 */
	public static void sendConfig(NPC asNpc, Player player) {
		Gliders g = forNpc(asNpc.getId());
		if (g == null) {
			return;
		}
		player.getVarsManager().sendVar(153, g.getConfig());
	}
	
	/**
	 * Gets the glider by the npc.
	 * @param npc the npc.
	 * @return the gliders.
	 */
	public static Gliders forNpc(int npc) {
		for (Gliders g : values()) {
			if (g.getNpc() == npc) {
				return g;
			}
		}
		return null;
	}
	
	/**
	 * Gets the button.
	 * @return The button.
	 */
	public int getButton() {
		return button;
	}

	/**
	 * Gets the WorldTile.
	 * @return The WorldTile.
	 */
	public WorldTile getLocation() {
		return WorldTile;
	}

	/**
	 * Gets the config.
	 * @return The config.
	 */
	public int getConfig() {
		return config;
	}

	/**
	 * Gets the flider value for the button id.
	 * @param id the id.
	 * @return the value.
	 */
	public static Gliders forId(int id) {
		for(Gliders i : Gliders.values()) {
			if(i.getButton() == id) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Gets the npc.
	 * @return the npc
	 */
	public int getNpc() {
		return npc;
	}

}
