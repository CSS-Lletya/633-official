package com.rs.cache;

import java.io.IOException;

import com.alex.store.Store;
import com.rs.GameProperties;

import lombok.SneakyThrows;

import com.rs.GameConstants;

public final class Cache {

	public static Store STORE;

	@SneakyThrows(IOException.class)
	public static void init()  {
		STORE = new Store(GameProperties.getGameProperties().getString("cache"));
	}

	public static final byte[] generateUkeysFile() {
		return STORE.generateIndex255Archive255Current(
				GameConstants.GRAB_SERVER_PRIVATE_EXPONENT,
				GameConstants.GRAB_SERVER_MODULUS);
	}
}