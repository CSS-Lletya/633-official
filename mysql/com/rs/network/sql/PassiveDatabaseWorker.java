package com.rs.network.sql;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.rs.GameConstants;
import com.rs.network.sql.model.Node;

import lombok.SneakyThrows;

/**
 * Perform passive database operations in the background.
 * Note: Do not use this worker for data that must be immediately inserted into the database.
 * 
 * @author Dennis
 * @version 1.3 25/8/23
 * @author Pb600
 * @version 1.2 17/03/13
 */
public class PassiveDatabaseWorker implements Runnable {
	
	/** Singleton **/
	private static PassiveDatabaseWorker singleton;
	
	/**
	 * Thread safe Queued Localhost Database operations models list
	 * Read in FIFO order.
	 */
	private ConcurrentLinkedQueue<Node> databaseModels = new ConcurrentLinkedQueue<Node>();
	
	/**
	 * Constructor
	 * throws RuntimeException in case {@code singleton} is already an object instance.
	 */
	public PassiveDatabaseWorker() {
		if (getSingleton() != null)
			throw new RuntimeException("Background worker is already created.");
	}
	
	/**
	 * Append a Database Access Object model to be processed.
	 * 
	 * @param databaseModel: DAO to be processed.
	 */
	public static void addRequest(Node databaseModel) {
		if (!GameConstants.SQL_ENABLED)
			return;
		if (getSingleton() != null) {
			if (!getSingleton().databaseModels.contains(databaseModel)) {
				getSingleton().databaseModels.add(databaseModel);
			}
		}
	}
	
	@SneakyThrows(Exception.class)
	@Override
	public void run() {
		while (true) {
			if (!databaseModels.isEmpty()) {
				Node databaseModel = null;
				while ((databaseModel = databaseModels.poll()) != null) {
					try {
						databaseModel.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						databaseModel = null;
					}
				}
			}
		}
	}
	
	private static PassiveDatabaseWorker getSingleton() {
		return singleton;
	}
}