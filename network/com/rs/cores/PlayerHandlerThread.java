package com.rs.cores;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.rs.game.player.Player;
import com.rs.utilities.GSONParser;

import io.vavr.control.Try;

/**
 * Handles concurrent state events of a player, including saving and logging out players.
 * <p>
 * This class is thread-safe and can be used to process player actions concurrently. It maintains
 * two separate queues for player saves and logouts, ensuring that these operations are handled
 * efficiently and safely.
 * </p>
 */
public class PlayerHandlerThread implements Runnable {

	private static ConcurrentLinkedQueue<Player> waitingLogoutPlayers = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<Player> waitingPlayerSaves = new ConcurrentLinkedQueue<>();

	@Override
	public final void run() {
		Try.run(() -> {
			processPlayerSaves();
			processPlayerLogouts();
		}).onFailure(Throwable::printStackTrace);
	}

	private void processPlayerSaves() {
		waitingPlayerSaves.forEach(PlayerHandlerThread::savePlayer);
	}

	public static void savePlayer(Player player) {
		String fileName = "data/characters/" + player.getDisplayName() + ".json";
		GSONParser.save(player, fileName, Player.class);
	}

	private void processPlayerLogouts() {
		waitingLogoutPlayers.forEach(this::logoutPlayer);
	}

	private void logoutPlayer(Player player) {
		savePlayer(player);
		player.getSession().forceLogout(player);
	}

	public static void addLogout(Player player) {
		waitingLogoutPlayers.add(player);
	}

	public static void addSave(Player player) {
		waitingPlayerSaves.add(player);
	}
	
	public static boolean exists(String username) {
		return new File("data/characters/" + username + ".json").exists();
	}
	
	public static Player loadPlayer(String username) {
		return GSONParser.load("data/characters/" + username + ".json", Player.class);
	}
}