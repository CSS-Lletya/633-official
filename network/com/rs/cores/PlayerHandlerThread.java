package com.rs.cores;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.rs.game.player.Player;
import com.rs.utilities.GSONParser;

import io.vavr.control.Try;

/**
 * Handles concurrent state events of a player
 */
public class PlayerHandlerThread implements Runnable {

    private static final ConcurrentLinkedQueue<Player> waitingLogoutPlayers = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Player> waitingPlayerSaves = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
		Try.run(() -> {
			processPlayerSaves();
			processPlayerLogouts();
		}).onFailure(fail -> fail.printStackTrace());
        processPlayerSaves();
        processPlayerLogouts();
    }

    private void processPlayerSaves() {
        waitingPlayerSaves.forEach(this::savePlayer);
    }

    private void savePlayer(Player player) {
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
}