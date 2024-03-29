package com.rs.net;

import java.io.File;

import com.rs.cores.PlayerHandlerThread;
import com.rs.game.player.Player;
import com.rs.utilities.GSONParser;

public class AccountCreation {

	public static Player loadPlayer(String username) {
		return GSONParser.load("data/characters/" + username + ".json", Player.class);
	}

	public static void savePlayer(Player player) {
		PlayerHandlerThread.addSave(player);
	}

	public static boolean exists(String username) {
		return new File("data/characters/" + username + ".json").exists();
	}

}
