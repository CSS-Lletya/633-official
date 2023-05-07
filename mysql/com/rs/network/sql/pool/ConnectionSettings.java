package com.rs.network.sql.pool;

import lombok.Data;

/**
 * Database connection settings used to
 * create a database connection, being able
 * to set a pool connection or not.
 * 
 * @author Pb600
 * 
 */
@Data
public class ConnectionSettings {
	
	private final String host;
	private final String database;
	private final String user;
	private final String password;
	private final int port;
	private final boolean pooled;
	private final long timeOutDelay;
	private final boolean persistent;
	private final int preferredPoolSize;
	private final int maxSize;
	private final int initialSize;
	private final int priorityConnections;

	/**
	 * @param host: Host of the database.
	 * @param database: Database name.
	 * @param user: User name to connect to the database.
	 * @param password: Password used to connect to database;
	 * @param port: Port used to connect to the database;
	 * @param pooled: Flag to determine if this database will handle pooled connections.
	 * @param timeOutDelay: Time out of connections;
	 * @param persistent: State if connections are persistent or not.
	 */
	public ConnectionSettings(String host, int port, String database, String user, String password, boolean pooled, int maxSize, int initialSize, int priorityConnections, int preferredPoolSize, long timeOutDelay, boolean persistent) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
		this.pooled = pooled;
		this.maxSize = maxSize;
		this.initialSize = initialSize;
		this.priorityConnections = priorityConnections;
		this.preferredPoolSize = preferredPoolSize;
		this.timeOutDelay = timeOutDelay;
		this.persistent = persistent;
	}
}