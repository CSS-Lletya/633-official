package com.rs.game.system.scripts.exceptions;

/**
 * An exception thrown when compiling an asc script.
 * @author 'Vexia
 *
 */
public class ScriptException extends Exception {

	/**
	 *  The serial version UID.
	 */
	private static final long serialVersionUID = -1328749105522163758L;

	/**
	 * The line index.
	 */
	protected final int lineId;

	/**
	 * Constructs a new {@code ScriptException} {@code Object}.
	 */
	public ScriptException() {
		this(-1);
	}
	
	/**
	 * Constructs a new {@code ScriptException} {@code Object}.
	 * @param lineId The parsing line id.
	 */
	public ScriptException(int lineId) {
		super();
		this.lineId = lineId;
	}
	
	/**
	 * Constructs a new {@code ScriptException} {@code Object}.
	 * @param message the message.
	 * @param lineId The parsing line id.
	 */
	public ScriptException(final String message, int lineId) {
		super("Error parsing line " + lineId + ": " + message);
		this.lineId = lineId;
	}

	/**
	 * Constructs a new {@code ScriptException} {@code Object}.
	 * @param message The error message.
	 * @param cause The cause.
	 */
	public ScriptException(String message, Throwable cause) {
		super(message, cause);
		lineId = -1;
	}
	
}
