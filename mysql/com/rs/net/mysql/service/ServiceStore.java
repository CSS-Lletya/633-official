package com.rs.net.mysql.service;

import com.google.common.collect.ImmutableList;
import com.rs.GameConstants;
import com.rs.net.mysql.service.impl.TestService;

/**
 * Represents a serviceable MYSQL task (query) to be sent.
 * @author Dennis
 *
 */
public final class ServiceStore {

	/**
	 * Execute the specified service
	 */
	public static void executeServiceTask(MYSQLService service) {
		if (GameConstants.SQL_ENABLED)
			service.execute();
	}
}