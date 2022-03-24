package com.rs.net.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rs.net.mysql.callback.ThreadedSQLCallback;

import io.vavr.control.Try;

/**
 * The main class which is used for all of the central operations
 * 
 * @author Nikki
 *
 */
public class ThreadedSQL {

	/**
	 * The service used to execute responses
	 */
	private ExecutorService service;

	/**
	 * The SQL Connection Pool
	 */
	private ConnectionPool<DatabaseConnection> pool;

	/**
	 * Create a new threaded sql instance from the specified configuraton
	 * 
	 * @param configuration
	 *            The configuration to use
	 */
	public ThreadedSQL(DatabaseConfiguration configuration) {
		service = Executors.newCachedThreadPool();
		pool = new ConnectionPool<DatabaseConnection>(configuration, 10);
	}

	/**
	 * Create a new threaded sql instance with the specified configuration and
	 * number of threads
	 * 
	 * @param configuration
	 *            The configuration to use
	 * @param threads
	 *            The max number of threads
	 */
	public ThreadedSQL(DatabaseConfiguration configuration, int threads) {
		this(configuration, threads, threads);
	}

	/**
	 * Create a new threaded sql instance with the specified configuration,
	 * number of threads and number of connections
	 * 
	 * @param configuration
	 *            The configuration to use
	 * @param threads
	 *            The max number of threads
	 * @param connections
	 *            The max number of connections
	 */
	public ThreadedSQL(DatabaseConfiguration configuration, int threads, int connections) {
		service = Executors.newFixedThreadPool(threads);
		pool = new ConnectionPool<>(configuration, threads);
	}

	/**
	 * Executed a PreparedStatement query.
	 * 
	 * @param statement
	 *            The statement to execute
	 * @param callback
	 *            The callback to inform when the query is successful/fails
	 */
	public void executeQuery(final PreparedStatement statement, final ThreadedSQLCallback callback) {
		service.execute(() -> Try.run(() -> query(statement, callback)));
	}

	/**
	 * Executed a standard sql query.
	 * 
	 * @param query
	 *            The statement to execute
	 * @param callback
	 *            The callback to inform when the query is successful/fails
	 */
	public void executeQuery(final String query, final ThreadedSQLCallback callback) {
		service.execute(() -> Try.run(() -> query(query, callback)));
	}

	/**
	 * Create a PreparedStatement from a random pool connection
	 * 
	 * @param string
	 *            The statement to prepare
	 * @return The initialized PreparedStatement
	 * @throws SQLException
	 *             If an error occurred while preparing
	 */
	public PreparedStatement prepareStatement(String string) throws SQLException {
		return (PreparedStatement) Try.of(pool::nextFree).onSuccess(success -> Try.run(() -> pool.nextFree().prepareStatement(string))).andFinally(() -> pool.nextFree().returnConnection());
	}

	/**
	 * Internal method to handle sql calls for PreparedStatements Note: You HAVE
	 * 
	 * @param statement
	 *            The statement to execute
	 * @param callback
	 *            The callback to inform
	 * @throws SQLException
	 *             If an error occurs while executing, this is passed to
	 *             callback.queryError(SQLException e)
	 */
	private void query(PreparedStatement statement, ThreadedSQLCallback callback) throws SQLException {
		statement.execute();
		Try.run(() -> callback.queryComplete(statement.getResultSet())).andFinally(() -> Try.run(() -> statement.getResultSet().close()));
	}

	/**
	 * Internal method to handle sql calls for standard responses
	 * 
	 * @param statement
	 *            The statement to execute
	 * @param callback
	 *            The callback to inform
	 * @throws SQLException
	 *             If an error occurs while executing, this is passed to
	 *             callback.queryError(SQLException e)
	 */
	private void query(String query, ThreadedSQLCallback callback) throws SQLException {
		DatabaseConnection conn = pool.nextFree();

		Connection c = conn.getConnection();

		Statement statement = c.createStatement();
		statement.execute(query);

		ResultSet result = statement.getResultSet();
		Try.run(() -> callback.queryComplete(result)).andFinally(() -> {
			Try.run(() -> {
				// Close the result set
				result.close();
				// Return the used connection
				conn.returnConnection();
			});
		});
	}

	/**
	 * Get the connection pool, for use with standard responses :D
	 */
	public ConnectionPool<DatabaseConnection> getConnectionPool() {
		return pool;
	}
}
