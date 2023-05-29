package com.rs.game.system.scripts;

/**
 * Represents the instruction types.
 * @author Emperor
 *
 */
public enum InstructionType {

	/**
	 * The condition type.
	 */
	CONDITION('$'),
	
	/**
	 * The instruction type.
	 */
	INSTRUCTION('*'),
	
	/**
	 * The method type.
	 */
	METHOD('#'),
	
	/**
	 * The local method type.
	 */
	LOCAL_METHOD('@'),
	
	/**
	 * The go to type.
	 */
	GOTO('>'), 
	
	/**
	 * The end bracket type.
	 */
	END_BRACKET('}')
	;
	
	/**
	 * Gets the instruction type.
	 * @param c The character.
	 * @return The instruction type.
	 */
	public static InstructionType forIndicator(char c) {
		for (InstructionType type : values()) {
			if (type.indication == c) {
				return type;
			}
		}
		return null;
	}
	
	/**
	 * The character indication.
	 */
	private final char indication;
	
	/**
	 * Constructs a new {@code InstructionType} {@code Object}.
	 * @param indication The indicator.
	 */
	private InstructionType(char indication) {
		this.indication = indication;
	}
	
	/**
	 * Gets the indication.
	 * @return The indication character.
	 */
	public char getIndication() {
		return indication;
	}
}