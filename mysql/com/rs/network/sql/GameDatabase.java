package com.rs.network.sql;

import com.rs.network.sql.pool.ConnectionSettings;
import com.rs.network.sql.pool.Database;

import lombok.Getter;

/**
 * Game database connections
 * 
 * @author Pb600
 * @author Dennis - improvements
 */
public class GameDatabase {

	/**
	 * Represents a synchronized collection of Database types
	 */
	@Getter
	public static Database gameServer;

	/**
	 * Initializes the Database service to the game Network
	 */
	public static void initializeWebsiteDatabases() {
		gameServer = new Database("Game Server", new ConnectionSettings("127.0.0.1", 3306, "open633-db", "root", "", true, 50, 1, 3, 3, 60000, true));
		gameServer.initializeSystem();
	}
}