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
		player.getPackets().sendSound(soundId, 0, 1);
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
			World.players().filter(p -> player.withinDistance(p, distance))
					.forEach(p -> sendSound(soundId));
		}
		return this;
	}
	
	/**
	 * Sends a sound to everyone ingame, usually nice to events taking place like
	 * shooting star or something similar.
	 * 
	 * @param soundId
	 * @param distance
	 * @return
	 */
	public AudioManager sendGlobalSound(int soundId) {
		World.players().forEach(p -> sendSound(soundId));
		return this;
	}
	
	/**
	 * Sends the index 2 sound version. This is complately different, and don't think it'll be used.
	 * @param soundId
	 * @return
	 */
	public AudioManager sendIndex2Sound(int soundId) {
		player.getPackets().sendSound(soundId, 0, 2);
		return this;
	}

	/**
	 * Sends a delayed sound effect
	 * TODO: Convert delayed tasks to use this method
	 * @param delay
	 * @param soundId
	 * @return
	 */
	public AudioManager sendSound(int delay, int soundId) {
		player.task(delay, p -> sendSound(soundId));
		return this;
	}
	
	/**
	 * Sends a delayed sound effect to nearby players
	 * @param delay
	 * @param soundId
	 * @param distance
	 * @return
	 */
	public AudioManager sendNearbyDelayedSound(int delay, int soundId, int distance) {
		player.task(delay, p -> sendNearbyPlayerSound(soundId, distance));
		return this;
	}
	
	/**
	 * Sends a delayed sound effect to everyone (globally to server)
	 * @param delay
	 * @param soundId
	 * @return
	 */
	public AudioManager sendGlobalDelayedSound(int delay, int soundId) {
		player.task(delay, p -> sendGlobalSound(soundId));
		return this;
	}
}