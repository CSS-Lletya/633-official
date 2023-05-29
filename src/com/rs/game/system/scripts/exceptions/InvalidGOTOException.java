package com.rs.game.system.scripts.exceptions;

/**
 * Thrown when a GOTO method name could not be found.
 * @author Emperor
 *
 */
public final class InvalidGOTOException extends ScriptException {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 8909510580401378793L;

	/**
	 * Constructs a new {@code InvalidGOTOException} {@code Object}.
	 * @param lineId The parsing line id.
	 */
	public InvalidGOTOException(int lineId) {
		super(lineId);
	}
	
	/**
	 * Constructs a new {@code InvalidGOTOException} {@code Object}.
	 * @param lineId The parsing line id.
	 */
	public InvalidGOTOException(String cause, int lineId) {
		super(cause, lineId);
	}
}