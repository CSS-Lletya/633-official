package com.rs.net.mysql.service;

import com.rs.net.mysql.DatabaseConfiguration;
import com.rs.net.mysql.DatabaseConnection;

import lombok.Data;

/**
 * A database configuration for MySQL Database servers
 * 
 * @author Nikki
 * 
 */
@Data
public class MySQLDatabaseConfiguration implements DatabaseConfiguration {

	/**
	 * The database server host
	 */
	private String host;

	/**
	 * The database server port
	 */
	private int port;

	/**
	 * The database on the server
	 */
	private String database;

	/**
	 * The username of the server
	 */
	private String username;

	/**
	 * The password of the server
	 */
	private String password;

	/**
	 * Create a new configuration
	 * 
	 * @param host
	 *            The host
	 * @param port
	 *            The port
	 * @param database
	 *            The database
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 */
	public MySQLDatabaseConfiguration(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	/**
	 * Create an empty connection and use setters to set the information
	 */
	public MySQLDatabaseConfiguration() {

	}

	@Override
	public DatabaseConnection newConnection() {
		return new MySQLDatabaseConnection(this);
	}
}