package com.rs.utilities;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tinylog.Logger;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.net.mysql.DatabaseConnection;

import lombok.SneakyThrows;

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
			submitFullQuery(message);
			break;
		}
	}

	/**
	 * Submit a full length query to the database
	 * 
	 * @param query
	 */
	@SneakyThrows(SQLException.class)
	public static void submitFullQuery(String query) {
		if (!checkSQLState())
			return;
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		Statement statement = connection.createStatement();
		if (statement == null) {
			return;
		}
		statement.executeUpdate(query);
		connection.returnConnection();
	}

	/**
	 * Submit a simple Log to the database
	 * 
	 * @param player
	 * @param query
	 */
	@SneakyThrows(SQLException.class)
	public static void submitSQLLog(Player player, String query) {
		if (!checkSQLState())
			return;
		DatabaseConnection connection = World.getConnectionPool().nextFree();
		Statement statement = connection.createStatement();
		if (statement == null) {
			return;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
		Date date = new Date();
		statement.executeUpdate("INSERT INTO log(username, logtext, date) VALUES ('" + player.getDisplayName() + "','"
				+ query + "','" + dateFormat.format(date) + "');");
		connection.returnConnection();
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