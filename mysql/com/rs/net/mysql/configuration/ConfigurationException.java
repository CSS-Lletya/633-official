package com.rs.net.mysql.configuration;

/**
 * An exception thrown if the value of the configuration is not the appropriate
 * type
 *
 * @author Nikki
 *
 */
public class ConfigurationException extends IllegalArgumentException {

	private static final long serialVersionUID = -2398143635720014938L;

	public ConfigurationException(String string) {
		super(string);
	}
}