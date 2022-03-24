package com.rs.net.mysql.service;

import java.sql.DriverManager;

import com.rs.net.mysql.DatabaseConnection;

import io.vavr.control.Try;

/**
 * An implementation of a <code>DatabaseConnection</code> which represents a
 * MySQL Connection
 * 
 * @author Nikki
 * 
 */
public class MySQLDatabaseConnection extends DatabaseConnection {

	/**
	 * Static constructor which loads our driver
	 */
	static {
		Try.run(() -> loadDriver("org.postgresql.Driver"));
	}

	/**
	 * Create a database connection instance
	 * 
	 * @param configuration
	 *            The database configuration
	 */
	public MySQLDatabaseConnection(MySQLDatabaseConfiguration configuration) {
		super(configuration);
	}

	/**
	 * Connect to the database
	 */
	public boolean connect() {
		return Try.run(() -> {
			MySQLDatabaseConfiguration configuration = (MySQLDatabaseConfiguration) this.configuration;
			connection = DriverManager.getConnection("jdbc:postgresql://" + configuration.getHost() + ":" + configuration.getPort() + "/" + configuration.getDatabase(), configuration.getUsername(), configuration.getPassword());
		}) != null;
	}
}