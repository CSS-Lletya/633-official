package com.rs.game.system.scripts;

/**
 * A script compiling entry.
 * @author Emperor
 *
 */
public class ScriptEntry {

	/**
	 * The current script context.
	 */
	private ScriptContext current;
	
	/**
	 * The previous script context.
	 */
	private ScriptEntry previousEntry;
	
	/**
	 * The instruction name.
	 */
	private String name;
	
	/**
	 * If a bracket was used.
	 */
	private boolean bracket;
	
	/**
	 * Constructs a new {@code ScriptEntry} {@code Object}.
	 * @param current The current script context.
	 * @param previous The previous script entry.
	 */
	public ScriptEntry(ScriptContext current, ScriptEntry previous, String name, boolean bracket) {
		this.current = current;
		this.previousEntry = previous;
		this.name = name;
		this.bracket = bracket;
	}

	/**
	 * Gets the current.
	 * @return The current.
	 */
	public ScriptContext getCurrent() {
		return current;
	}

	/**
	 * Sets the current.
	 * @param current The current to set.
	 */
	public void setCurrent(ScriptContext current) {
		this.current = current;
	}

	/**
	 * Gets the previous.
	 * @return The previous.
	 */
	public ScriptEntry getPreviousEntry() {
		return previousEntry;
	}

	/**
	 * Sets the previous.
	 * @param previous The previous to set.
	 */
	public void setPrevious(ScriptEntry previous) {
		this.previousEntry = previous;
	}

	/**
	 * Gets the bracket.
	 * @return The bracket.
	 */
	public boolean isBracket() {
		return bracket;
	}

	/**
	 * Sets the bracket.
	 * @param bracket The bracket to set.
	 */
	public void setBracket(boolean bracket) {
		this.bracket = bracket;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}