package com.rs.cache;

import java.io.IOException;

import com.alex.store.Store;
import com.rs.GameProperties;
import com.rs.GameConstants;

public final class Cache {

	public static Store STORE;

	public static void init() throws IOException {
		STORE = new Store(GameProperties.getGameProperties().getString("cache"));
	}

	public static final byte[] generateUkeysFile() {
		return STORE.generateIndex255Archive255Current(
				GameConstants.GRAB_SERVER_PRIVATE_EXPONENT,
				GameConstants.GRAB_SERVER_MODULUS);
	}
}