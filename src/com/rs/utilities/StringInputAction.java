package com.rs.utilities;

/**
 * Handles String value inputs
 * @author Dennis
 *
 */
public abstract class StringInputAction {

	/**
	 * Used to dynamically handle a integer input, saves time and is much
	 * cleaner than the traditional way of doing it
	 *
	 * @param input
	 */
	public abstract void handle(String input);
}