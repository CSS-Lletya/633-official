package com.rs.network.sql.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rs.network.sql.pool.ConnectionListener.ConnectionState;

public class DatabaseConnection {

	private static final Logger logger = Logger.getLogger(ConnectionSettings.class.getName());
	
	private final ConnectionSettings connectionSettings;
	private ConnectionListener connectionListener;
	private Connection connection;
	private boolean connectionOpened;
	private final String connectionLink;
	private String connectionHash;
	private Statement statement;
	private boolean priorityConnection;
	private boolean closeOnRelease;
	private boolean busy;
	private long releaseTime;
	private long connectTime;
	private long expectedLifeTime;
	
	public DatabaseConnection(ConnectionSettings connectionSettings) {
		this.connectionSettings = connectionSettings;
		connectionLink = "jdbc:mysql://" + connectionSettings.getHost() + ":" + connectionSettings.getPort() + "/" + connectionSettings.getDatabase() +"?connectTimeout="+2500+"&rewriteBatchedStatements=true";
	}
	
	public void openConnection() {
		setConnectionOpened(false);
		try {
			connection = DriverManager.getConnection(connectionLink, connectionSettings.getUser(), connectionSettings.getPassword());
			statement = connection.createStatement();
			setConnectionOpened(true);
		} catch (SQLException e) {
			reportError(e);
		}
		
	}
	
	private void reportError(SQLException e) {
		if (e.getErrorCode() == 1045) {
			logger.log(Level.SEVERE, e.getMessage());
		} else {
			logger.log(Level.SEVERE, "Error creating database connection: " + e.getMessage() + "(" + e.getErrorCode() + ")");
			e.printStackTrace();
		}
	}
	
	protected void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error closing database connection: " + e.getMessage());
			e.printStackTrace();
		} finally {
			setConnectionOpened(false);
		}
	}
	
	public PreparedStatement prepareStatement(String query) {
		if (connection == null) {
			System.err.println("Invalid connection");
			return null;
		}
		try {
			return connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
	/**
	 * Release a connection and close the connection if
	 * necessary.
	 * 
	 * @param close: True to close the connection after releasing.
	 */
	public void release(boolean close) {
		//		System.out.println("Releasing connection...");
		releaseTime = System.currentTimeMillis();
		if (!connectionSettings.isPersistent() || close) {
			closeConnection();
		}
		if (connectionListener != null) {
			connectionListener.connectionStateChange(this, ConnectionState.RELEASED);
		}
		setBusy(false);
	}
	
	/**
	 * Release this connection to be re-used
	 * It's advised to close all prepared statements
	 * before release the connection.
	 */
	public void release() {
		release(false);
	}
	
	public void setBusy(boolean busy) {
		this.busy = busy;
		if (busy) {
			releaseTime = 0;
			connectTime = System.currentTimeMillis();
		}
	}
	
	public void assureConnection() {
		
		if (!isConnected()) {
			openConnection();
		}
	}
	
	public boolean isConnected() {
		if (connection == null) {
			return false;
		}
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isTimedOut() {
		if (releaseTime > 0)
			return System.currentTimeMillis() - releaseTime > connectionSettings.getTimeOutDelay();
		return false;
	}
	
	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	public void close(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ResultSet executeQuery(String query) {
		assureConnection();
		if (query != null && !query.isEmpty()) {
			if (statement != null) {
				try {
					return statement.executeQuery(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public DatabaseConnection executeUpdate(String query) {
		assureConnection();
		if (query != null && !query.isEmpty()) {
			if (statement == null) {
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}
	
	public boolean isConnectionOpened() {
		return connectionOpened;
	}
	
	private void setConnectionOpened(boolean connected) {
		this.connectionOpened = connected;
	}
	
	@Override
	public String toString() {
		return connectionSettings.toString();
	}
	
	public void commit() {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setAutoCommit(boolean autoCommit) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.setAutoCommit(autoCommit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public long getExpectedLifeTime() {
		return expectedLifeTime;
	}
	
	public void setExpectedLifeTime(long expectedLifeTime) {
		this.expectedLifeTime = expectedLifeTime;
	}
	
	public boolean hasExcedeedLifeTime() {
		if (getExpectedLifeTime() == 0) {
			return false;
		}
		return (System.currentTimeMillis() - connectTime) > getExpectedLifeTime();
	}

	public void closeStatement() {
		if(statement != null) {
			close(statement);
		}
	}

	public boolean isCloseOnRelease() {
		return closeOnRelease;
	}

	public void setCloseOnRelease(boolean closeOnRelease) {
		this.closeOnRelease = closeOnRelease;
	}

	public boolean isPriorityConnection() {
		return priorityConnection;
	}

	public void setPriorityConnection(boolean priorityConnection) {
		this.priorityConnection = priorityConnection;
	}

	public String getConnectionHash() {
		return connectionHash;
	}

	public void setConnectionHash(String connectionHash) {
		this.connectionHash = connectionHash;
	}
}