package com.rs.network.sql.pool;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.rs.GameConstants;

/**
 * Server database manager, provide services based on connection pools.
 * The connection pool allows to use already opened connections,
 * and prevent waste of time opening too many connections,
 * a connection is created when no any connection is available for use.
 * 
 * @author Pb600
 * 
 */
public class Database {
	
	private static Logger logger = Logger.getLogger(Database.class.getName());
	
	private final String managerName;
	private ConnectionPool connectionPool;
	private DatabaseConnection connection;

	private final ConnectionSettings databaseConfiguration;
	
	public Database(String managerName, ConnectionSettings databaseConfiguration) {
		this.managerName = managerName;
		this.databaseConfiguration = databaseConfiguration;
	}
	
	public void reportUnavailableConnection() {
		throw new RuntimeException("No connection available in the connection pool:\n" + connectionPool.toString());
	}
	
	public void initializeSystem() {
		checkDatabaseDriver();
		connectionPool = new ConnectionPool(databaseConfiguration);
		connectionPool.initializeConnections();
		DatabaseWatcher databaseWatcher = new DatabaseWatcher(databaseConfiguration.getDatabase(), connectionPool);
		Thread databaseWatcherThread = new Thread(databaseWatcher, "Database Watcher-Thread");
		databaseWatcherThread.setPriority(Thread.MIN_PRIORITY);
		databaseWatcherThread.setDaemon(true);
		databaseWatcherThread.start();
	}
	
	private void checkDatabaseDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "MySQL Driver was not found.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a database connection if available any
	 * is available in the pool, or get {@code connection} in case this database handler is not pooled.
	 * 
	 * @param timeout: Max time to wait for a connection.
	 * @return: Available DatabaseConnection;
	 */
	public DatabaseConnection getConnection(long timeout, String connectionHash) {
		if (databaseConfiguration.isPooled()) {
			return connectionPool.getConnection(timeout, connectionHash);
		}
		return connection;
	}
	
	public DatabaseConnection getPriorityConnection(long timeout, String connectionHash) {
		if (databaseConfiguration.isPooled()) {
			return connectionPool.getConnection(timeout, true, connectionHash);
		}
		return connection;
	}
	
	public int releaseDeadConnections() {
		if (databaseConfiguration.isPooled()) {
			return connectionPool.releaseDeadConnections();
		}
		return -1;
	}
	
	/**
	 * Get a database connection and returns null if there's no connection
	 * available.
	 * 
	 * @return: Available DatabaseConnection
	 */
	public DatabaseConnection getConnection(String connectionHash) {
		return getConnection(-1, connectionHash);
	}
	
	public void releaseConnections(){
		if (connectionPool != null)
			connectionPool.releaseConnections();
	}
	
	/**
	 * Debug connection pool data
	 * @return: Connection pool debug information
	 */
	public String getPoolData() {
		if (connectionPool != null)
			return "--------------------" + managerName + "--------------------\n" + connectionPool.toString();
		return "";
	}
	
	public void debug() {
		if (GameConstants.DEBUG)
			System.out.println("Total connections in the pool: " + connectionPool.totalConnection());
	}
	
}
