package com.rs.game.system.scripts.exceptions;

/**
 * An exception thrown when the interpreter could not be found.
 * @author Emperor
 *
 */
public final class InvalidInterpreterException extends ScriptException {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 6096860966849544737L;

	/**
	 * Constructs a new {@code InvalidInterpreterException} {@code Object}.
	 *
	 */
	public InvalidInterpreterException() {
		super();
	}
	
	/**
	 * Constructs a new {@code InvalidInterpreterException} {@code Object}.
	 * @param lineId The line id.
	 */
	public InvalidInterpreterException(int lineId) {
		super(lineId);
	}
	
	/**
	 * Constructs a new {@code InvalidInterpreterException} {@code Object}.
	 * @param cause The cause.
	 * @param lineId The parsing line id.
	 */
	public InvalidInterpreterException(String cause, int lineId) {
		super(cause, lineId);
	}
	
}