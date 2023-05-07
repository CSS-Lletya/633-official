package com.rs.network.sql.pool;

import io.vavr.control.Try;

/**
 * Watch for inactive connections and close them.
 * 
 * @author Pb600
 * 
 */
public class DatabaseWatcher implements Runnable {
	
	private static final int SERVICE_TIMER = 30 * (1000);
	private final ConnectionPool connectionPool;
	
	private boolean isRunning;
	
	public DatabaseWatcher(String databaseName, ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
		this.isRunning = true;
	}
	
	public void interrupt() {
		isRunning = false;
	}
	
	@Override
	public void run() {
		while (isRunning()) {
			for (DatabaseConnection connection : connectionPool.getDatabaseConnections()) {
				if (connection.isConnectionOpened() && connection.isTimedOut()) {
					//System.out.println("Closed connection timmed out.");
					connection.closeConnection();
				}
			}
			sleepThread(SERVICE_TIMER);
		}
	}
	
	private void sleepThread(long time) {
		Try.run(() -> Thread.sleep(time)).onFailure(Throwable::printStackTrace);
	}
	
	private boolean isRunning() {
		return isRunning;
	}
}