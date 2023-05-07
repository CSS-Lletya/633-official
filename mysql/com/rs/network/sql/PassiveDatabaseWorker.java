package com.rs.network.sql;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.rs.GameConstants;
import com.rs.network.sql.model.Node;

/**
 * Perform passive database operations in the background.
 * Note: Do not use this worker for data that must be immediately inserted into the database.
 * 
 * @author Pb600
 * @version 1.2 17/03/13
 */
public class PassiveDatabaseWorker implements Runnable {
	
	/** Cycle time **/
	private static final int SLEEP_TIMER = 600;
	
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
		if (getSingleton() != null) {
			throw new RuntimeException("Background worker is already created.");
		}
	}
	
	/**
	 * Initialize singleton and start thread in minimum priority.
	 */
	public static void initialize() {
		if (getSingleton() != null) {
			throw new RuntimeException("Background worker is already created.");
		}
		singleton = new PassiveDatabaseWorker();
		Thread thread = new Thread(singleton, "Passive database worker thread.");
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
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
	
	@Override
	public void run() {
		while (true) {
			try {
				if (!databaseModels.isEmpty()) {
					Node databaseModel = null;
					while ((databaseModel = databaseModels.poll()) != null) {
						try {
							databaseModel.query();
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							databaseModel = null;
						}
					}
				}
				Thread.sleep(SLEEP_TIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static PassiveDatabaseWorker getSingleton() {
		return singleton;
	}
}