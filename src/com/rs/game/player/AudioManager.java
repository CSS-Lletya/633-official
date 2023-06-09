package com.rs.game.player;

import com.rs.game.map.World;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * A utility class that represents the sending of sounds to a Player or globally
 * Remember to define a definition for each Sound for easy-to-ready.
 * @author Dennis
 *
 */
public class AudioManager {
	
	/**
	 * Represents the Player receiving the sound effect
	 */
	private transient Player player;
	
	/**
	 * The initial delay of the sound being delivered. 
	 * Majority of the time it'll always be 0.
	 */
	private int delay = 0;

	/**
	 * Constructs a new Audio Manager for the Player
	 * @param player
	 */
	public AudioManager(Player player) {
		this.player = player;
	}
	
	/**
	 * Sends a simple sound to the Player
	 * @param soundId
	 * @return
	 */
	public AudioManager sendSound(int soundId) {
		player.getPackets().sendSound(soundId, delay, 1);
		return this;
	}
	
	/**
	 * Sends a simple sound to all players within the distance
	 * Usually used in group activities, or if someone is teleporting next to you.
	 * @param soundId
	 * @return
	 */
	public AudioManager sendNearbyPlayerSound(int soundId, int distance) {
		ObjectArrayList<Short> playerIndexes = World.getRegion(player.getRegionId()).getPlayersIndexes();
		if (playerIndexes != null) {
			World.players().filter(p -> player.withinDistance(p))
					.forEach(p -> sendSound(soundId));
		}
		return this;
	}
	
	/**
	 * Sends the index 2 sound version. This is complately different, and don't think it'll be used.
	 * @param soundId
	 * @return
	 */
	public AudioManager sendIndex2Sound(int soundId) {
		player.getPackets().sendSound(soundId, delay, 2);
		return this;
	}
	
	/**
	 * Sets the specified delay if required
	 * @param delayBy
	 * @return
	 */
	public AudioManager setDelay(int delayBy) {
		this.delay = delayBy;
		return this;
	}
}