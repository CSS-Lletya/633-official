package com.rs.net.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import io.vavr.control.Try;

/**
 * An abstract DatabaseConnection, which can have many types
 * 
 * @author Nikki
 * 
 */
public abstract class DatabaseConnection {

	/**
	 * The configuration of this connection
	 */
	protected DatabaseConfiguration configuration;

	/**
	 * The connection pool this connection belongs to
	 */
	private ConnectionPool<DatabaseConnection> pool;

	/**
	 * The last use of this connection
	 */
	private long lastUse = System.currentTimeMillis();

	/**
	 * The actual connection
	 */
	protected Connection connection = null;

	/**
	 * Create a database connection with the specified configuration
	 * 
	 * @param configuration
	 *            The configuration
	 */
	public DatabaseConnection(DatabaseConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Create a database connection with the specified configuration belonging
	 * to the specified pool
	 * 
	 * @param configuration
	 *            The configuration
	 * @param pool
	 *            The pool
	 */
	public DatabaseConnection(DatabaseConfiguration configuration, ConnectionPool<DatabaseConnection> pool) {
		this.configuration = configuration;
		this.pool = pool;
	}

	/**
	 * Connect to a database (Overridden by classes)
	 * 
	 * @return True, if conected
	 */
	public abstract boolean connect();

	/**
	 * Load a driver by providing a method, cleaner than all those catches!
	 * 
	 * @param driverName
	 *            The class name
	 */
	public static void loadDriver(String driverName) {
		Try.run(() -> Class.forName(driverName));
	}

	/**
	 * Close this connection
	 */
	public void close() {
		Try.run(() -> connection.close());
	}

	/**
	 * Create a connection statement
	 * 
	 * @return The statement
	 * @throws SQLException
	 *             If the connection is closed
	 */
	public Statement createStatement() throws SQLException {
		if (connection == null) {
			throw new SQLException("Database not connected yet!");
		}
		if (connection.isClosed()) {
			throw new SQLException("Connection closed!");
		}
		return connection.createStatement();
	}

	/**
	 * Create a prepared statement
	 * 
	 * @param string
	 *            The statement
	 * @return The prepared statement
	 * @throws SQLException
	 *             ?
	 */
	public PreparedStatement prepareStatement(String string) throws SQLException {
		return connection.prepareStatement(string);
	}

	/**
	 * Get the connection
	 * 
	 * @return The connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Check if this connection is closed
	 * 
	 * @return True, if this connection was closed
	 */
	public boolean isClosed() {
		Try.run(() -> connection.isClosed());
		return true;
	}

	/**
	 * Check that this connection is fresh and not closed
	 * 
	 * @return True, if the connection is valid
	 */
	public boolean isFresh() {
		return (System.currentTimeMillis() - lastUse < 60000) && !isClosed();
	}

	/**
	 * Return a connection to the pool, short for
	 * ConnectionPool.returnConnection
	 */
	public void returnConnection() {
		lastUse = System.currentTimeMillis();
		if (pool != null)
			pool.returnConnection(this);
	}

	/**
	 * Set this connection's pool
	 * 
	 * @param connectionPool
	 *            The pool
	 */
	public void setPool(ConnectionPool<DatabaseConnection> connectionPool) {
		this.pool = connectionPool;
	}
}