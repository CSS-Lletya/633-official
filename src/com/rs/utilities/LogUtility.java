package com.rs.utilities;

import org.tinylog.Logger;

import com.rs.GameConstants;
import com.rs.network.sql.PassiveDatabaseWorker;
import com.rs.network.sql.queries.SQLLogSQLPlugin;

/**
 * Represents a feature-rich Logging utility backed by TinyLog
 * <https://tinylog.org/v2/> <https://tinylog.org/v2/logging/>
 * 
 * @author Dennis
 *
 */
public final class LogUtility {

	/**
	 * Represents the type of the Log we're sending
	 * 
	 * @author Dennis
	 *
	 */
	public enum LogType {
		INFO, TRACE, DEBUG, WARN, ERROR, SQL
	}

	/**
	 * Submits a specified log to the console.
	 * 
	 * @param logType
	 * @param message
	 */
	public static void log(LogType logType, String message) {
		switch (logType) {
		case DEBUG:
			Logger.debug(message);
			break;
		case ERROR:
			Logger.error(message);
			break;
		case INFO:
			Logger.info(message);
			break;
		case TRACE:
			Logger.trace(message);
			break;
		case WARN:
			Logger.warn(message);
			break;
		case SQL:
			submitSQLLog(message);
			break;
		}
	}

	/**
	 * Submit a simple Log to the database
	 * 
	 * @param player
	 * @param query
	 */
	private static void submitSQLLog(String query) {
		if (!checkSQLState())
			return;
		PassiveDatabaseWorker.addRequest(new SQLLogSQLPlugin(query));
	}

	/**
	 * Checks the state of the SQL from startup
	 * 
	 * @return state
	 */
	private static boolean checkSQLState() {
		if (!GameConstants.SQL_ENABLED)
			System.out.println("Unable to process request, MYSQL services are not present.");
		return GameConstants.SQL_ENABLED;
	}
}