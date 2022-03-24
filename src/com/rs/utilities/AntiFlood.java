package com.rs.utilities;

import com.rs.GameConstants;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Anti Flood
 * 
 * @Author Apache Ah64
 */
public final class AntiFlood {

	private static ObjectArrayList<String> connections = new ObjectArrayList<String>(GameConstants.PLAYERS_LIMIT * 100);

	public static void add(String ip) {
		connections.add(ip);
	}

	public static void remove(String ip) {
		connections.remove(ip);
	}

	public static int getSessionsIP(String ip) {
		int amount = 1;
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).equalsIgnoreCase(ip))
				amount++;
		}
		return amount;
	}
}