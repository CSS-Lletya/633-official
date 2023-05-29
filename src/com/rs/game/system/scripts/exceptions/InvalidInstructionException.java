package com.rs.game.system.scripts.exceptions;

/**
 * Thrown when an instruction could not be found.
 * @author Emperor
 *
 */
public class InvalidInstructionException extends ScriptException {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -7969980063835245652L;

	/**
	 * Constructs a new {@code InvalidInstructionException} {@code Object}.
	 * @param lineId The parsing line id.
	 */
	public InvalidInstructionException(int lineId) {
		super(lineId);
	}
	
	/**
	 * Constructs a new {@code InvalidInstructionException} {@code Object}.
	 * @param lineId The parsing line id.
	 */
	public InvalidInstructionException(String cause, int lineId) {
		super(cause, lineId);
	}
}