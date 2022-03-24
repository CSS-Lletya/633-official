package com.rs.net.mysql.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The callback class used for SQL responses
 * 
 * @author Nikki
 *
 */
public interface ThreadedSQLCallback {
	
	/**
	 * Called when a query is complete
	 * @param result
	 * 			The result, or null if it was an UPDATE/DELETE.
	 * @throws SQLException
	 * 			If an error occurs in the user implemented code.
	 */
	void queryComplete(ResultSet result) throws SQLException;

	/**
	 * Called when a query fails.
	 * @param e
	 * 			The exception thrown
	 */
	void queryError(SQLException e);
	
}
