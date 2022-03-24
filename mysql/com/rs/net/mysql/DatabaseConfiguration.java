package com.rs.net.mysql;

public interface DatabaseConfiguration {

	/**
	 * Create a new database connection
	 *
	 * @return The new connection
	 */
	DatabaseConnection newConnection();

}