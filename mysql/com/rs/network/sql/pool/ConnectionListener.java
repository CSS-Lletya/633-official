package com.rs.network.sql.pool;

public interface ConnectionListener {
	
	public static enum ConnectionState {
		OPENED, CLOSED, RELEASED, TIMED_OUT;
	}
	
	public void connectionStateChange(DatabaseConnection databaseConnection, ConnectionState connectionState);
}
