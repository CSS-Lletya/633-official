package com.rs.network.sql.pool;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Pool of database connections. The class provide connections awaiting to be used, create connections when needed and destroy idle connections.
 * 
 * @author Pb600
 * 
 */
public class ConnectionPool implements ConnectionListener {

	private final ConcurrentLinkedQueue<DatabaseConnection> databaseConnections = new ConcurrentLinkedQueue<DatabaseConnection>();
	private final ConcurrentLinkedQueue<DatabaseConnection> priorityConnections = new ConcurrentLinkedQueue<DatabaseConnection>();
	private int connectionRequests;
	private int poolSize;

	private final ConnectionSettings connectionSettings;

	/**
	 * Start a pool with specified settings.
	 * 
	 * @param connectionSettings
	 *            : Settings to be specified to this pool of connections.
	 */
	public ConnectionPool(ConnectionSettings connectionSettings) {
		this.connectionSettings = connectionSettings;
	}

	/**
	 * Get a safe thread list of pool connections.
	 * 
	 * @return: Connections in the pool.
	 */
	protected ConcurrentLinkedQueue<DatabaseConnection> getDatabaseConnections() {
		return databaseConnections;
	}

	/**
	 * Initialize pool connections.
	 */
	public void initializeConnections() {
		for (int connectionID = 0; connectionID < connectionSettings.getInitialSize(); connectionID++) {
			addConnection(true, false);
		}
	}

	/**
	 * Get an available connection if there is any, otherwise create a new connection. In case of amount of connections are over the limit, await {@code timeout} for a connection to become available, otherwise return nulls;
	 * 
	 * @param timeout
	 *            : Max time to await for a connection in case there's no one available.
	 * @return: A database connection.
	 */
	public DatabaseConnection getConnection(long timeout, String connectionHash) {
		return getConnection(timeout, false, connectionHash);
	}

	public DatabaseConnection getConnection(long timeout, boolean priorityConnection, String connectionHash) {
		synchronized (this) {
			connectionRequests++;
			int maxSize = connectionSettings.getMaxSize() - connectionSettings.getPriorityConnections();
			boolean hasSpace = poolSize < maxSize;
			if (!hasSpace && priorityConnection) {
				for (DatabaseConnection databaseConnection : priorityConnections) {
					if (!databaseConnection.isBusy()) {
						databaseConnection.setConnectionHash(connectionHash);
						databaseConnection.assureConnection();
						databaseConnection.setPriorityConnection(true);
						databaseConnection.setBusy(true);
						return databaseConnection;
					}
				}
				DatabaseConnection databaseConnection;
				if (priorityConnections.size() < connectionSettings.getPriorityConnections()) {
					databaseConnection = addConnection(true, true);
					databaseConnection.setConnectionHash(connectionHash);
					databaseConnection.assureConnection();
					databaseConnection.setPriorityConnection(true);
					databaseConnection.setBusy(true);
				} else {
					databaseConnection = new DatabaseConnection(connectionSettings);
					databaseConnection.setConnectionHash(connectionHash);
					databaseConnection.openConnection();
					databaseConnection.setCloseOnRelease(true);
					databaseConnection.setConnectionListener(this);
				}
				return databaseConnection;
			} else {
				if (poolSize <= 0) {
					addConnection(true, false);
				}
				for (DatabaseConnection databaseConnection : databaseConnections) {
					if (!databaseConnection.isBusy() && (databaseConnection.isConnected() || hasSpace)) {
						databaseConnection.assureConnection();
						databaseConnection.setBusy(true);
						databaseConnection.setConnectionHash(connectionHash);
						return databaseConnection;
					}
				}
				if (poolSize < maxSize) {
					DatabaseConnection databaseConnection = addConnection(true, false);
					databaseConnection.setBusy(true);
					databaseConnection.setConnectionHash(connectionHash);
					return databaseConnection;
				}
			}
			if (timeout < 0)
				return null;
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return getConnection(-1, connectionHash);// After time-out, attempt to get another connection without time-out

	}

	/**
	 * Get an available database connection, if there's no available connections returns null.
	 * 
	 * @return: Available Database connection.
	 */
	public DatabaseConnection getConnection(String connectionHash) {
		return getConnection(-1, connectionHash);
	}

	/**
	 * Remove a Database connection from the pool.
	 * 
	 * @param databaseConnection
	 *            : Connection to be removed from the pool.
	 */
	public void removeConnection(DatabaseConnection databaseConnection) {
		synchronized (this) {
			boolean removed = databaseConnections.remove(databaseConnection);
			if (removed) {
				poolSize--;
			}
		}
	}

	/**
	 * Insert a connection to the pool. Opens the connection up on insert if necessary.
	 * 
	 * @param openConnection
	 *            : State if the connection should be opened or just added to the pool.
	 * @return: The connection inserted to the pool.
	 */
	public DatabaseConnection addConnection(boolean openConnection, boolean priorityConnection) {
		synchronized (this) {
			DatabaseConnection databaseConnection = new DatabaseConnection(connectionSettings);
			if (openConnection) {
				databaseConnection.openConnection();
			}
			databaseConnection.setConnectionListener(this);
			boolean added;
			if (priorityConnection) {
				added = priorityConnections.add(databaseConnection);
			} else {
				added = databaseConnections.add(databaseConnection);
			}
			if (added) {
				poolSize++;
			}
			return databaseConnection;
		}
	}

	/**
	 * Handle connection state changes, In case there's more connections than preferred, then remove released connections.
	 */
	@Override
	public void connectionStateChange(DatabaseConnection databaseConnection, ConnectionState connectionState) {
		if (connectionState == ConnectionState.RELEASED || connectionState == ConnectionState.TIMED_OUT) {
			synchronized (this) {
				if (poolSize > connectionSettings.getPreferredPoolSize()) {
					removeConnection(databaseConnection);
				}
				notifyAll();
			}
		}
	}

	/**
	 * Get the total amount of connections in the pool.
	 * 
	 * @return: Amount of connections in the pool.
	 */
	public int totalConnection() {
		synchronized (this) {
			return poolSize;
		}
	}

	/**
	 * Release connections that have exceeded expected lifetime.
	 */
	public int releaseDeadConnections() {
		int connectionsReleased = 0;
		for (DatabaseConnection connection : databaseConnections) {
			if (connection != null) {
				if (connection.hasExcedeedLifeTime() || !connection.isConnectionOpened()) {
					connection.release(true);
					connectionsReleased++;
				}
			}
		}
		return connectionsReleased;
	}

	public String toString() {
		StringBuilder debug = new StringBuilder();
		debug.append("Connections in the pool: ").append(poolSize);
		debug.append("\n");

		int busyConnections = 0;
		int openedConnections = 0;
		ObjectArrayList<String> connectionHashes = new ObjectArrayList<String>();
		for (DatabaseConnection databaseConnection : databaseConnections) {
			if (databaseConnection.isConnected() && databaseConnection.isBusy()) {
				busyConnections++;
			}
			if(databaseConnection.isBusy()) {
				connectionHashes.add(databaseConnection.getConnectionHash());
			}
			if (databaseConnection.isConnectionOpened()) {
				openedConnections++;
			}
		}
		
		debug.append("Busy connections: ").append(+busyConnections);
		debug.append("\n");
		debug.append("Active connections: ").append(connectionHashes.toString());
		debug.append("\n");
		debug.append("Opened connections: ").append(openedConnections);
		debug.append("\n");
		debug.append("Closed connections: ").append(poolSize - openedConnections);
		debug.append("\n");
		debug.append("Connection requests: ").append(connectionRequests);
		return debug.toString();
	}

	public void releaseConnections() {
		for (DatabaseConnection connection : databaseConnections) {
			if (connection != null) {
				connection.release(true);
			}
		}
	}

	public String getActiveConnections() {
		ObjectArrayList<String> connectionHashes = new ObjectArrayList<String>();
		for (DatabaseConnection databaseConnection : databaseConnections) {
			if(databaseConnection.isBusy()) {
				connectionHashes.add(databaseConnection.getConnectionHash());
			}
		}
		return connectionHashes.toString();
	}
}